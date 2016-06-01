package tgm.shakeit.quakewatchaustria;


import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * Fragment that displays a listview with the quake data.
 *
 * @author Moritz Mühlehner
 * @version 2016-06-01.1
 */
public class OneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lv;
    private ArrayList<Erdbeben> values;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tabname;
    private Location mLastLocation;

    /**
     * Gets called when the view is inflated
     *
     * @param inflater           the inflater
     * @param container          the container that contains the fragment
     * @param savedInstanceState the saved instance state
     * @return the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.one_fragment, container, false);
        values = (ArrayList<Erdbeben>) getArguments().getSerializable("values");
        tabname = getArguments().getString("tabname");
        mLastLocation = getArguments().getParcelable("mLastLocation");
        lv = (ListView) v.findViewById(R.id.listAt);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        lv.setBackgroundColor(Color.WHITE);
        CustomArrayAdapter adapter = new CustomArrayAdapter(getContext(), values);
        lv.setAdapter(adapter);
        lv.deferNotifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Erdbeben temp = (Erdbeben) parent.getItemAtPosition(position);
                Intent i = new Intent(getContext(), DetailPage.class);
                i.putExtra("data", temp);
                startActivity(i);
            }
        });
        return v;
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Refresh().execute();
    }

    /**
     * Async task for refreshing the list's data
     */
    private class Refresh extends AsyncTask<String, String, String> {

        boolean updated = true;

        /**
         * The background task
         *
         * @param params the parameters
         * @return string
         */
        @Override
        protected String doInBackground(String... params) {
            JSONLoader jp = new JSONLoader(JSONLoader.AT);
            if (tabname.equalsIgnoreCase("AT"))
                jp = new JSONLoader(JSONLoader.AT);
            if (tabname.equalsIgnoreCase("EU"))
                jp = new JSONLoader(JSONLoader.EU);
            if (tabname.equalsIgnoreCase("WORLD"))
                jp = new JSONLoader(JSONLoader.WORLD);
            JSONArray tmp;
            try {
                tmp = jp.getjObj().getJSONArray("features");
                values.clear();
                for (int i = 0; i < tmp.length(); i++)
                    values.add(new Erdbeben(tmp.getJSONObject(i), mLastLocation));
            } catch (Exception e) {
                updated = false;
            }
            return null;
        }

        /**
         * Gets called after the background task was executed
         *
         * @param strFromDoInBg string
         */
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            swipeRefreshLayout.setRefreshing(false);
            if (updated) {
                CustomArrayAdapter adapter = new CustomArrayAdapter(getContext(), values);
                lv.setAdapter(adapter);
                lv.deferNotifyDataSetChanged();
            }
            if (!updated)
                Toast.makeText(getActivity(), "Keine Datenverbindung", Toast.LENGTH_SHORT).show();
        }
    }
}