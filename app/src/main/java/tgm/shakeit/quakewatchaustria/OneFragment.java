package tgm.shakeit.quakewatchaustria;

/**
 * Created by Moritz on 21.04.2016.
 */
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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


public class OneFragment extends Fragment {

    private ListView lv;
    private View v;
    private ArrayList<Erdbeben> values;

    public OneFragment(ArrayList<Erdbeben> v){
        this.values=v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.one_fragment, container, false);
        lv = (ListView) v.findViewById(R.id.listAt);
        lv.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> adapter = new CustomArrayAdapter(getContext(),values);
        lv.setAdapter(adapter);
        lv.deferNotifyDataSetChanged();
        return v;
    }

    public ListView getLv() {
        return lv;
    }

    public void setLv(ListView lv) {
        this.lv = lv;
    }

    public ArrayList<Erdbeben> getValues() {
        return values;
    }

    public void setValues(ArrayList<Erdbeben> values) {
        this.values = values;
    }



}

