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
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.vision.Detector;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Fragment that manages three sub-fragments. Displays the viewpager with the three quake tabs.
 *
 * @author Moritz Mühlehner, Daniel May
 * @version 2016-05-29.1
 */
public class QuakeLists extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FloatingActionButton.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OneFragment at, eu, welt;
    private Location mLastLocation;
    private ArrayList<Erdbeben> atvalues;
    private ArrayList<Erdbeben> euvalues;
    private ArrayList<Erdbeben> weltvalues;
    private ArrayList<Erdbeben> latest;
    private FileManager<ArrayList<Erdbeben>> fm;
    private GoogleApiClient mGoogleApiClient;
    private boolean contentcreated=false;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabpage, container, false);
        if(!contentcreated) {
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
                    at = new OneFragment(atvalues, "AT", null);
                    eu = new OneFragment(euvalues, "EU", null);
                    welt = new OneFragment(weltvalues, "WORLD", null);
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
            } else {
                new Operation().execute();
            }
            contentcreated=true;
        }
        else{
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(at, "AT");
        adapter.addFragment(eu, "EU");
        adapter.addFragment(welt, "Welt");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "Keine Datenverbindung", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

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
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    })
                    .setCancelable(true)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        new LatestOperation().execute();
    }

    class Operation extends AsyncTask<String, String, String> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONLoader jp;
            if (atvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.AT);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++) {
                        atvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                    }
                } catch (Exception e) {
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
                }
            }
            if (euvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.EU);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++) {
                        euvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                    }
                } catch (Exception e) {
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
                }
            }
            if (weltvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.WORLD);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++) {
                        weltvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                    }
                } catch (Exception e) {
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
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            fm.writeObject(FileManager.AT_FILE, atvalues, getContext());
            fm.writeObject(FileManager.EU_FILE, euvalues, getContext());
            fm.writeObject(FileManager.WORLD_FILE, weltvalues, getContext());
            at = new OneFragment(atvalues, "AT", mLastLocation);
            eu = new OneFragment(euvalues, "EU", mLastLocation);
            welt = new OneFragment(weltvalues, "WORLD", mLastLocation);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    class LatestOperation extends AsyncTask<String, String, String> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Aktuelle Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONLoader jp;
            if(latest.isEmpty()){
                jp = new JSONLoader(JSONLoader.LATEST);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++) {
                        latest.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                    }
                } catch (Exception e) {
                }
            }
            return null;

        }
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            if(!latest.isEmpty()) {
                Intent i = new Intent(getContext(), ReferenzBeben.class);
                i.putExtra("latest", latest);
                i.putExtra("loc", mLastLocation);
                startActivity(i);
            }
            if(latest.isEmpty()){
                Toast.makeText(getActivity(),"Keine Datenverbindung",Toast.LENGTH_LONG).show();
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}