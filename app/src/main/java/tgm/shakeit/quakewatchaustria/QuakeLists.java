package tgm.shakeit.quakewatchaustria;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that manages three sub-fragments. Displays the viewpager with the three quake tabs.
 *
 * @author Moritz Mühlehner, Daniel May
 * @version 2016-05-29.1
 */
public class QuakeLists extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FloatingActionButton.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 100;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OneFragment at, eu, welt;
    private Location mLastLocation;
    private ArrayList<Erdbeben> atvalues;
    private ArrayList<Erdbeben> euvalues;
    private ArrayList<Erdbeben> weltvalues;
    private ArrayList<LatestQuake> latest;
    private FileManager<ArrayList<Erdbeben>> fm;
    private GoogleApiClient mGoogleApiClient;
    private boolean contentcreated = false;
    private static final String TAG = "QuakeLists.java";

    /**
     * Checks if the network is available
     *
     * @param context the application's context
     * @return if the network is available
     */
    private static boolean isNetworkAvailable(Context context) {
        boolean outcome = false;
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null)  // connected to the internet
                outcome = true;
        }
        return outcome;
    }

    /**
     * Gets called when the fragment is inflated
     *
     * @param inflater           the inflater
     * @param container          the container that contains the fragment
     * @param savedInstanceState the saved instance state
     * @return the fragment's view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabpage, container, false);
        if (!contentcreated) {
            atvalues = new ArrayList<>();
            weltvalues = new ArrayList<>();
            euvalues = new ArrayList<>();
            latest = new ArrayList<>();
            fm = new FileManager<>();
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
            if (!isNetworkAvailable(getContext())) {
                if (fm.readObject(FileManager.AT_FILE, getContext()) != null &&
                        fm.readObject(FileManager.EU_FILE, getContext()) != null &&
                        fm.readObject(FileManager.WORLD_FILE, getContext()) != null) {
                    atvalues = fm.readObject(FileManager.AT_FILE, getContext());
                    euvalues = fm.readObject(FileManager.EU_FILE, getContext());
                    weltvalues = fm.readObject(FileManager.WORLD_FILE, getContext());
                    at = new OneFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("values", atvalues);
                    args.putString("tabname", "AT");
                    args.putParcelable("mLastLocation", null);
                    at.setArguments(args);
                    eu = new OneFragment();
                    Bundle args2 = new Bundle();
                    args2.putSerializable("values", euvalues);
                    args2.putString("tabname", "EU");
                    args2.putParcelable("mLastLocation", null);
                    eu.setArguments(args2);
                    welt = new OneFragment();
                    Bundle args3 = new Bundle();
                    args3.putSerializable("values", weltvalues);
                    args3.putString("tabname", "WORLD");
                    args3.putParcelable("mLastLocation", null);
                    welt.setArguments(args3);
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    try {
                        new AlertDialog.Builder(getContext())
                                .setTitle("No internet connection")
                                .setMessage("Please turn on mobile data")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }

                                })
                                .setCancelable(false)
                                .show();
                    } catch (Exception e) {
                        Log.d("", "Show Dialog: " + e.getMessage());
                    }
                }
            } else
                new Operation().execute();
            contentcreated = true;
        } else {
            viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                }
            });

        }

        getActivity().setTitle(R.string.quake_list);
        return rootView;
    }

    /**
     * Gets called when the connection fails
     *
     * @param connectionResult the connection's result
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    /**
     * Gets called on connection success
     *
     * @param connectionHint the connection hint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        int hasLocationPermissions = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermissions != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_CODE);
                            }
                        });
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CODE);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    /**
     * Shows a message dialog
     *
     * @param okListener the onClick listener
     */
    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Berechtigungen werden für die Abfrage Ihres Standorts benötigt")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Getter for the last location
     *
     * @return the last location
     */
    public Location getmLastLocation() {
        return mLastLocation;
    }

    /**
     * Gets called on request permissions
     *
     * @param requestCode  the request code
     * @param permissions  the permissions
     * @param grantResults the grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        return;
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                } else  // Permission Denied
                    Toast.makeText(getActivity(), "Standort konnte nicht abgerufen werden", Toast.LENGTH_SHORT)
                            .show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Sets up the view pager
     *
     * @param viewPager the view pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(at, "AT");
        adapter.addFragment(eu, "EU");
        adapter.addFragment(welt, "Welt");
        viewPager.setAdapter(adapter);
    }

    /**
     * Gets called when the connection is suspended.
     *
     * @param i i
     */
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "Keine Datenverbindung", Toast.LENGTH_LONG).show();
    }

    /**
     * Gets called on resume
     */
    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    /**
     * Gets called on stop
     */
    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Gets called on start
     */
    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            new AlertDialog.Builder(getContext())
                    .setTitle("No internet connection")
                    .setMessage("Please turn on mobile data")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /**
                         * Handles clicks
                         * @param dialog the dialog interface
                         * @param which which
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    })
                    .setCancelable(true)
                    .show();
        }
    }

    /**
     * On clikc method for loading latest quakes
     *
     * @param v the view that is clicked on
     */
    @Override
    public void onClick(View v) {
        new LatestOperation().execute();
    }

    /**
     * Task for loading the latest refernce quakes
     */
    private class Operation extends AsyncTask<String, String, String> {

        private ProgressDialog mDialog;
        private boolean loaded;

        /**
         * Gets called before executing the background task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        /**
         * background activity
         *
         * @param params the params
         * @return null
         */
        @Override
        protected String doInBackground(String... params) {
            loaded=true;
            JSONLoader jp;
            if (atvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.AT);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++)
                        atvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                } catch (Exception e) {
                    loaded=false;
                }
            }
            if (euvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.EU);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++)
                        euvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                } catch (Exception e) {
                    loaded=false;
                }
            }
            if (weltvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.WORLD);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++)
                        weltvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                } catch (Exception e) {
                    loaded=false;
                }
            }
            return null;
        }

        /**
         * Gets called after executing the background task
         *
         * @param strFromDoInBg string
         */
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            if(loaded) {
                fm.writeObject(FileManager.AT_FILE, atvalues, getContext());
                fm.writeObject(FileManager.EU_FILE, euvalues, getContext());
                fm.writeObject(FileManager.WORLD_FILE, weltvalues, getContext());
                at = new OneFragment();
                Bundle args = new Bundle();
                args.putSerializable("values", atvalues);
                args.putString("tabname", "AT");
                args.putParcelable("mLastLocation", mLastLocation);
                at.setArguments(args);
                eu = new OneFragment();
                Bundle args2 = new Bundle();
                args2.putSerializable("values", euvalues);
                args2.putString("tabname", "EU");
                args2.putParcelable("mLastLocation", mLastLocation);
                eu.setArguments(args2);
                welt = new OneFragment();
                Bundle args3 = new Bundle();
                args3.putSerializable("values", weltvalues);
                args3.putString("tabname", "WORLD");
                args3.putParcelable("mLastLocation", mLastLocation);
                welt.setArguments(args3);
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }
            else{
                Toast.makeText(getContext(),"Erdbeben konnten nicht geladen werden",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Task for fetching the latest reference quakes
     */
    private class LatestOperation extends AsyncTask<String, String, String> {

        private ProgressDialog mDialog;

        /**
         * Gets called before executing the background task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Aktuelle Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        /**
         * background activity
         *
         * @param params the params
         * @return null
         */
        @Override
        protected String doInBackground(String... params) {
            JSONLoader jp;
            if (latest.isEmpty()) {
                jp = new JSONLoader(JSONLoader.LATEST);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++)
                        latest.add(new LatestQuake(tmp.getJSONObject(i)));
                } catch (Exception e) {
                    Log.e(TAG, "Error while fetching latest quakes: " + e.getMessage());
                }
            }
            return null;
        }

        /**
         * Gets called after executing the background task
         *
         * @param strFromDoInBg string
         */
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            if (!latest.isEmpty()) {
                Intent i = new Intent(getContext(), ReferenzBeben.class);
                i.putExtra("latest", latest);
                if (mLastLocation == null) {
                    i.putExtra("lon", 0.0);
                    i.putExtra("lat", 0.0);
                } else {
                    i.putExtra("lon", mLastLocation.getLongitude());
                    i.putExtra("lat", mLastLocation.getLatitude());
                }
                startActivity(i);
            }
            if (latest.isEmpty())
                Toast.makeText(getActivity(), "Keine Datenverbindung", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * View Pager Adapter
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        /**
         * Initiates the ViewPager Adapter
         *
         * @param manager the fragment manager
         */
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        /**
         * Returns fragment on desired position
         *
         * @param position the position
         * @return the fragment
         */
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        /**
         * Gets the fragment lists size
         *
         * @return size
         */
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        /**
         * Adds a fragment
         *
         * @param fragment fragment
         * @param title    title
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /**
         * Get the page title
         *
         * @param position the position
         * @return the title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}