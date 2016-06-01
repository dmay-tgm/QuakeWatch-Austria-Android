package tgm.shakeit.quakewatchaustria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Activity that displays a map with reference quakes.
 *
 * @author Moritz Mühlehner
 * @version 2016-06-01.1
 */
public class ReferenzBeben extends AppCompatActivity implements Serializable, OnMapReadyCallback {

    private ArrayList<LatestQuake> data;
    private MapView mapView;
    private double lat, lon;

    /**
     * Gets called when the activity is created
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referenzbeben);
        data = (ArrayList<LatestQuake>) getIntent().getExtras().getSerializable("latest");
        lat = (double) getIntent().getExtras().getSerializable("lat");
        lon = (double) getIntent().getExtras().getSerializable("lon");
        mapView = (MapView) findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
        FloatingActionButton quakefound = (FloatingActionButton) findViewById(R.id.quakefound);
        if (quakefound != null)
            quakefound.setOnClickListener(new QuakeFoundListener());
        FloatingActionButton quakenotfound = (FloatingActionButton) findViewById(R.id.noquakefound);
        if (quakenotfound != null)
            quakenotfound.setOnClickListener(new QuakeNotFoundListener());
    }

    /**
     * Gets called when the mapview is ready
     *
     * @param googleMap the google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (LatestQuake lq : data)
            createMarker(lq.getLatitude(), lq.getLongitude(), googleMap, lq.getId());
        if (lon != 0 && lat != 0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 5));
        googleMap.setOnMarkerClickListener(new QuakeMarkerListener());
    }

    /**
     * Creates a marker at the specified map and location
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     * @param googleMap the google map
     * @param id        the marker's id
     */
    private void createMarker(double latitude, double longitude, GoogleMap googleMap, String id) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(id));
    }

    /**
     * Gets called on resume
     */
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    /**
     * Gets called on destroy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Gets called on low memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * OnClickListener for the floating action button
     */
    private class QuakeFoundListener implements FloatingActionButton.OnClickListener {
        /**
         * Handles a click on the FAB
         *
         * @param v the view that is clicked on
         */
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Bitte klicken Sie auf das Erdbeben, das Sie verspürt haben", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Listener for markers on a map
     */
    private class QuakeMarkerListener implements GoogleMap.OnMarkerClickListener {
        /**
         * Gets called when a marker is called
         *
         * @param marker the marker that is clicked on
         * @return true
         */
        @Override
        public boolean onMarkerClick(final Marker marker) {
            String id = marker.getTitle();
            Report.setReference(id);
            Intent i = new Intent(getApplicationContext(), LocationPage.class);
            i.putExtra("now", false);
            startActivity(i);
            return true;
        }
    }

    /**
     * Second OnClickListener for another FAB
     */
    private class QuakeNotFoundListener implements FloatingActionButton.OnClickListener {
        /**
         * Handles a click on the fab.
         *
         * @param v the view that is clicked on
         */
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), LocationPage.class);
            i.putExtra("now", false);
            startActivity(i);
        }
    }
}