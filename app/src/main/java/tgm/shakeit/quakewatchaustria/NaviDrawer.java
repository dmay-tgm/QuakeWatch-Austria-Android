package tgm.shakeit.quakewatchaustria;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class NaviDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    
    private ViewPager viewPager;
    private OneFragment at, eu, welt;
    private Context c;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ArrayList<Erdbeben> atvalues;
    private ArrayList<Erdbeben> euvalues;
    private ArrayList<Erdbeben> weltvalues;
    private JSONLoader jp;
    TabLayout tabLayout;
    FileManager<ArrayList<Erdbeben>> fm;

    public static boolean isNetworkAvailable(Context context) {
        boolean outcome = false;

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo tempNetworkInfo : networkInfos) {

                if (tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }

        return outcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        atvalues = new ArrayList<Erdbeben>();
        weltvalues = new ArrayList<Erdbeben>();
        euvalues = new ArrayList<Erdbeben>();
        setContentView(R.layout.activity_navi_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm=new FileManager<ArrayList<Erdbeben>>();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (!isNetworkAvailable(getBaseContext())) {
            if(fm.readObject(fm.AT_FILE,getBaseContext())!=null &&
                    fm.readObject(fm.EU_FILE,getBaseContext())!=null &&
                    fm.readObject(fm.WORLD_FILE,getBaseContext())!=null) {
                atvalues = fm.readObject(fm.AT_FILE, getBaseContext());
                euvalues = fm.readObject(fm.EU_FILE, getBaseContext());
                weltvalues = fm.readObject(fm.WORLD_FILE, getBaseContext());
                at = new OneFragment(atvalues, "AT", null);
                eu = new OneFragment(euvalues, "EU", null);
                welt = new OneFragment(weltvalues, "WORLD", null);
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            } else {
                try {
                    new AlertDialog.Builder(this)
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
        } else{
            new Operation().execute();
        }
        JodaTimeAndroid.init(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(at, "AT");
        adapter.addFragment(eu, "EU");
        adapter.addFragment(welt, "Welt");
        viewPager.setAdapter(adapter);
    }

    /**
     * Handles a press on the back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navi_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles clicks on the navigation items
     *
     * @param item selected MenuItem
     * @return true
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.quake_list) {
//
//        } else if (id == R.id.behavior_advisor) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
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
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(this, "Location empfangen", Toast.LENGTH_LONG);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Location abrufen nicht möglich", Toast.LENGTH_LONG);
    }


    class Operation extends AsyncTask<String, String, String> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(NaviDrawer.this);
            mDialog.setMessage("Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(atvalues.isEmpty()) {
                jp = new JSONLoader(JSONLoader.AT);
                JSONArray tmp;
                try {
                    tmp = jp.getjObj().getJSONArray("features");
                    for (int i = 0; i < tmp.length(); i++) {
                        atvalues.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                    }
                } catch (Exception e) {
                    Toast.makeText(getParent(), "Fehler bei der Verbindung", Toast.LENGTH_LONG);
                    mDialog.setMessage("Beben konnten nicht geladen werden...");
                    try {
                        mDialog.wait(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
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
                    Toast.makeText(getParent(), "Fehler bei der Verbindung", Toast.LENGTH_LONG);
                    mDialog.setMessage("Beben konnten nicht geladen werden...");
                    try {
                        mDialog.wait(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
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
                    Toast.makeText(getParent(), "Fehler bei der Verbindung", Toast.LENGTH_LONG);
                    mDialog.setMessage("Beben konnten nicht geladen werden...");
                    try {
                        mDialog.wait(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            fm.writeObject(fm.AT_FILE,atvalues,getBaseContext());
            fm.writeObject(fm.EU_FILE,euvalues,getBaseContext());
            fm.writeObject(fm.WORLD_FILE,weltvalues,getBaseContext());
            at = new OneFragment(atvalues,"AT",mLastLocation);
            eu = new OneFragment(euvalues,"EU",mLastLocation);
            welt = new OneFragment(weltvalues,"WORLD",mLastLocation);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
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
