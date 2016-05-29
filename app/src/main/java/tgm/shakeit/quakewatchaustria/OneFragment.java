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
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
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


public class OneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ListView lv;
    private View v;
    private ArrayList<Erdbeben> values;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tabname;
    private Location mLastLocation;

    public OneFragment(ArrayList<Erdbeben> v, String tabname, Location mLastLocation){
        this.values=v;
        this.tabname=tabname;
        this.mLastLocation=mLastLocation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.one_fragment, container, false);
        lv = (ListView) v.findViewById(R.id.listAt);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
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

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Refresh().execute();
    }
    private class Refresh extends AsyncTask<String,String,String>{

        boolean updated=true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONLoader jp = new JSONLoader(JSONLoader.AT);
            if(tabname.equalsIgnoreCase("AT"))
                jp=new JSONLoader(JSONLoader.AT);
            if(tabname.equalsIgnoreCase("EU"))
                jp=new JSONLoader(JSONLoader.EU);
            if(tabname.equalsIgnoreCase("WORLD"))
                jp=new JSONLoader(JSONLoader.WORLD);
            JSONArray tmp;
            try {
                tmp = jp.getjObj().getJSONArray("features");
                values.clear();
                for (int i = 0; i < tmp.length(); i++) {
                    values.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
                }
            } catch (Exception e) {
                updated=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            swipeRefreshLayout.setRefreshing(false);
            if(updated) {
                ArrayAdapter<String> adapter = new CustomArrayAdapter(getContext(), values);
                lv.setAdapter(adapter);
                lv.deferNotifyDataSetChanged();
            }
            if(!updated) {
                Toast.makeText(getActivity(),"Keine Datenverbindung",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

