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
public class ReferenzBeben extends AppCompatActivity implements Serializable, OnMapReadyCallback {

    private ArrayList<LatestQuake> data;
    private MapView mapView;
    private double lat,lon;
    private FloatingActionButton quakefound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referenzbeben);
        data = (ArrayList<LatestQuake>) getIntent().getExtras().getSerializable("latest");
        lat = (double) getIntent().getExtras().getSerializable("lat");
        lon = (double) getIntent().getExtras().getSerializable("lon");
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        quakefound = (FloatingActionButton) findViewById(R.id.quakefound);
        quakefound.setOnClickListener(new QuakeFoundListener());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for(LatestQuake lq:data){
            createMarker(lq.getLatitude(),lq.getLongitude(),googleMap);
        }
        if(lon!=0 && lat!=0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),5));
    }

    private void createMarker(double latitude, double longitude, GoogleMap googleMap) {
         googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
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

    class QuakeFoundListener implements FloatingActionButton.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(),"Bitte klicken Sie auf das Erdbeben das Sie verspührt haben", Toast.LENGTH_LONG).show();
        }
    }
}
