package tgm.shakeit.quakewatchaustria;

import android.location.Location;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Moritz
 */
public class ReferenzBeben extends AppCompatActivity implements Serializable, OnMapReadyCallback, FloatingActionButton.OnClickListener {

    private ArrayList<Erdbeben> data;
    private MapView mapView;
    private Location loc;
    private FloatingActionButton quakefound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referenzbeben);
        data = (ArrayList<Erdbeben>) getIntent().getExtras().getSerializable("latest");
        loc = (Location) getIntent().getExtras().getSerializable("loc");
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        quakefound = (FloatingActionButton) findViewById(R.id.quakefound);
        quakefound.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(data.get(0).getLatitude(),data.get(0).getLatitude())).title(data.get(0).getTime()+"this is a marker"));
        if(loc!=null)

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(),loc.getLongitude()),5));
    }

    private void createMarker(double latitude, double longitude, GoogleMap googleMap) {
         googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Bitte klicken Sie auf das Erdbeben das Sie verspührt haben", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
