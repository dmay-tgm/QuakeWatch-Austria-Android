package tgm.shakeit.quakewatchaustria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComicArrayAdapter extends ArrayAdapter<Comic> {

    private Context context;
    private int layoutResourceId;
    private Comic[] data = null;

    public ComicArrayAdapter(Context context, int layoutResourceId, Comic[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ComicHolder holder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ComicHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.comicView);
            holder.txtTitle = (TextView) row.findViewById(R.id.report_title);
            holder.txtDetail = (TextView) row.findViewById(R.id.report_detail);
            row.setTag(holder);
        } else {
            holder = (ComicHolder) row.getTag();
        }
        Comic comic = data[position];
        holder.txtTitle.setText(comic.getTitle());
        holder.txtDetail.setText(comic.getDetail());
        holder.imgIcon.setImageResource(comic.getIcon());

        return row;
    }

    static class ComicHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDetail;
    }
}