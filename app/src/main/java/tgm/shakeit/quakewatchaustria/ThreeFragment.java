package tgm.shakeit.quakewatchaustria;

/**
 * Created by Moritz on 21.04.2016.
 */
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ThreeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ListView lv;
    private View v;
    private ArrayList<Erdbeben> values;
    private JSONLoader jp;
    private static final String TAG = "JSONLoader.java";
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        this.context=this.getContext();
        values=new ArrayList<Erdbeben>();
        jp=null;
        v = inflater.inflate(R.layout.one_fragment, container, false);
        lv = (ListView) v.findViewById(R.id.listAt);
        lv.setBackgroundColor(Color.WHITE);
        new Operation().execute();
        return v;
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
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
        Toast.makeText(getContext(), "Location empfangen", Toast.LENGTH_LONG);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "Location abrufen nicht möglich", Toast.LENGTH_LONG);
    }


    class Operation extends AsyncTask<String,String,String>{

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jp = new JSONLoader(JSONLoader.WORLD);
            JSONArray tmp;
            try {
                tmp = jp.getjObj().getJSONArray("features");
                for (int i = 0; i < tmp.length(); i++) {
                    values.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                }
            } catch (Exception e) {
                Log.e(TAG, "Couldn't interpret the data: " + e.toString());
                Toast.makeText(getContext(),"Fehler bei der Verbindung",Toast.LENGTH_LONG);
                mDialog.setMessage("Beben konnten nicht geladen werden...");
                try {
                    mDialog.wait(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            ArrayAdapter<String> adapter = new CustomArrayAdapter(getContext(),values);
            lv.setAdapter(adapter);
            lv.deferNotifyDataSetChanged();
        }
    }

}

