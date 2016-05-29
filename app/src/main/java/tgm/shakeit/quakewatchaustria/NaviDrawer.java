package tgm.shakeit.quakewatchaustria;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import net.danlew.android.joda.JodaTimeAndroid;

public class NaviDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private NavigationView navigationView;
    private QuakeLists ql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_drawer);
        ql=new QuakeLists();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        JodaTimeAndroid.init(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.quake_list));
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navigationView.setCheckedItem(id);
//        navigationView.getMenu().findItem(id).setChecked(true);
        Fragment newFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.imprint) {
            newFragment = new Imprint();
            webView = null;
        } else if (id == R.id.quake_list) {
            newFragment = ql;
            webView = null;
        } else {
            newFragment = new WebPage();

            Bundle args = new Bundle();
            args.putInt("toLoad", id);
            args.putString("title", String.valueOf(item.getTitle()));
            newFragment.setArguments(args);
        }
        transaction.replace(R.id.main_content, newFragment, "content_frag");
        transaction.addToBackStack(null);
        transaction.commit();

        getSupportFragmentManager().executePendingTransactions();


        Fragment f = getSupportFragmentManager().findFragmentByTag("content_frag");
        if (f != null) {
            View v = f.getView();
            if (v != null) {
                WebView web = (WebView) v.findViewById(R.id.webView);
                if (web != null) {
                    webView = web;
                } else
                    webView = null;
            } else
                webView = null;
        } else
            webView = null;
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView != null && webView.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            webView.goBack();
            // go back to previous page
            return true;
        } else {
            finish();
            // finish the activity
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}