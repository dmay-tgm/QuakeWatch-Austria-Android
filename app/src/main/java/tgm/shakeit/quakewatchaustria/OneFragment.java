package tgm.shakeit.quakewatchaustria;

/**
 * Created by Moritz on 21.04.2016.
 */
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OneFragment extends Fragment {

    ListView lv;
    View v;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.one_fragment, container, false);
        lv = (ListView) v.findViewById(R.id.listAt);
        ArrayList<Erdbeben> values=new ArrayList<>();
        /*values.add(new Erdbeben(5.2,"Wien","Wien","27.04.2016","19:24"));
        values.add(new Erdbeben(3.1,"Wien","Wien","27.04.2016","19:32"));
        values.add(new Erdbeben(4.3,"Wien","Wien","27.04.2016","19:24"));
        values.add(new Erdbeben(1.6,"Wien","Wien","27.04.2016","19:24"));
        values.add(new Erdbeben(9.4,"Wien","Wien","27.04.2016","19:24"));
        values.add(new Erdbeben(7.8,"Wien","Wien","27.04.2016","19:24"));
        values.add(new Erdbeben(6.9,"Wien","Wien","27.04.2016","19:24"));*/
        ArrayAdapter<String> adapter = new CustomArrayAdapter(getContext(),values);
        lv.setAdapter(adapter);
        lv.deferNotifyDataSetChanged();
        lv.setBackgroundColor(Color.WHITE);
        return v;
    }
}

