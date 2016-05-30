package tgm.shakeit.quakewatchaustria;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

/**
 * Created by Moritz
 */
public class DetailPage extends AppCompatActivity implements Serializable, OnMapReadyCallback {

    private Erdbeben data;
    private MapView mapView;
    private GoogleMap map;
    private TextView vmag;

    public final static String[] colorCodes = {
            //Green
            "#66BB6A", "#4CAF50", "#43A047",
            //Yellow
            "#FFEE58", "#FFEB3B", "#FDD835",
            //Orange
            "#FFA726", "#FF9800", "#FB8C00",
            //Blue
            "#5C6BC0", "#3F51B5", "#3949AB",
            //Purple
            "#673AB7", "#5E35B1",
            //Red
            "#C62828"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (Erdbeben) getIntent().getExtras().getSerializable("data");
        setContentView(R.layout.detailpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Detailansicht");
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        vmag = (TextView) findViewById(R.id.listText);
        TextView region;
        TextView ort = (TextView) findViewById(R.id.textViewOrt);
        TextView date = (TextView) findViewById(R.id.textViewDatum);
        TextView time = (TextView) findViewById(R.id.textViewTime);
        double mag = data.getMag();
        this.setColor(mag);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        vmag.setText(new DecimalFormat("#0.0", dfs).format(mag));
        date.setText(data.getDate());
        time.setText(data.getTime());
        if (data.getOrt() != null && !data.getOrt().equals("")) {
            region = (TextView) findViewById(R.id.textViewLocation);
            region.setText(data.getRegion());
            ((TextView) findViewById(R.id.textViewLocation2)).setText("");
            ort.setText(data.getOrt());
        } else {
            region = (TextView) findViewById(R.id.textViewLocation2);
            region.setText(data.getRegion());
            ((TextView) findViewById(R.id.textViewLocation)).setText("");
            ort.setText("");
        }
        TextView coords = (TextView) findViewById(R.id.textViewCoords);
        coords.setText(data.getCords());
        TextView depth = (TextView) findViewById(R.id.textViewDepth);
        depth.setText(data.getDepth());
        TextView dist1 = (TextView) findViewById(R.id.textViewDist1);
        TextView dist2 = (TextView) findViewById(R.id.textViewDist2);
        TextView dist3 = (TextView) findViewById(R.id.textViewDist3);
        TextView dist4 = (TextView) findViewById(R.id.textViewDist4);
        dist4.setText(data.getDistance());
        ArrayList<String> dists=data.getDist();
        dist1.setText(dists.get(0));
        dist2.setText(dists.get(1));
        dist3.setText(dists.get(2));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(data.getLatitude(),data.getLongitude())).title(data.getMag()+""));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.getLatitude(),data.getLongitude()),5));
        map=googleMap;
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

    public void setColor(double mag){
        if ((mag >= 0) && (mag <= 2.49)) {
            if ((mag >= 0) && (mag <= 1.49)) {
                vmag.setTextColor(Color.parseColor(colorCodes[0]));
            }
            if ((mag >= 1.50) && (mag <= 1.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[1]));

            }
            if ((mag >= 2.00) && (mag <= 2.49)) {
                vmag.setTextColor(Color.parseColor(colorCodes[2]));

            }

        } /*NEXT COLOR*/ else if ((mag >= 2.50) && (mag <= 3.99)) {
            if ((mag >= 2.50) && (mag <= 2.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[2]));

            }
            if ((mag >= 3.0) && (mag <= 3.49)) {
                vmag.setTextColor(Color.parseColor(colorCodes[4]));

            }
            if ((mag >= 3.5) && (mag <= 3.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[5]));

            }

        }/*NEXT COLOR*/ else if ((mag >= 4) && (mag <= 5.49)) {
            if ((mag >= 4) && (mag <= 4.49)) {
                vmag.setTextColor(Color.parseColor(colorCodes[6]));

            }
            if ((mag >= 4.5) && (mag <= 4.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[7]));

            }
            if ((mag >= 5) && (mag <= 5.49)) {
                vmag.setTextColor(Color.parseColor(colorCodes[8]));

            }

        }/*NEXT COLOR*/ else if ((mag >= 5.5) && (mag <= 6.99)) {
            if ((mag >= 5.5) && (mag <= 5.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[9]));

            }
            if ((mag >= 6) && (mag <= 6.49)) {
                vmag.setTextColor(Color.parseColor(colorCodes[10]));

            }
            if ((mag >= 6.5) && (mag <= 6.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[11]));

            }

        }/*NEXT COLOR*/ else if ((mag >= 7) && (mag <= 8.99)) {
            if ((mag >= 7) && (mag <= 7.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[12]));

            }
            if ((mag >= 8) && (mag <= 8.99)) {
                vmag.setTextColor(Color.parseColor(colorCodes[14]));

            }
        }/*NEXT COLOR*/ else if ((mag >= 9) && (mag <= 12)) {
            vmag.setTextColor(Color.parseColor(colorCodes[14]));

        }
    }
}
