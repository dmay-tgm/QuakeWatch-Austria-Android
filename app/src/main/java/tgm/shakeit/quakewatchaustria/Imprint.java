package tgm.shakeit.quakewatchaustria;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Daniel on 28.05.2016.
 */
public class Imprint extends Fragment {
    public Imprint() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imprint, container, false);
        ((TextView) rootView.findViewById(R.id.team)).setText(Html.fromHtml(getString(R.string.team)));
        ((TextView) rootView.findViewById(R.id.tgm_title)).setText(Html.fromHtml(getString(R.string.tgm_title)));
        ((TextView) rootView.findViewById(R.id.zamg_address)).setText(Html.fromHtml(getString(R.string.zamg_address)));
        ((TextView) rootView.findViewById(R.id.zamg_phone)).setText(Html.fromHtml(getString(R.string.zamg_phone)));
        ((TextView) rootView.findViewById(R.id.zamg_mail)).setText(Html.fromHtml(getString(R.string.zamg_mail)));
        TextView tv = (TextView) rootView.findViewById(R.id.zamg_hint);
        tv.setText(Html.fromHtml(getString(R.string.zamg_hint)));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv2 = (TextView) rootView.findViewById(R.id.agb);
        tv2.setText(Html.fromHtml(getString(R.string.agb)));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
        getActivity().setTitle(R.string.imprint);
        return rootView;
    }
}