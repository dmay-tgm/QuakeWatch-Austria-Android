package tgm.shakeit.quakewatchaustria;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Maps an array of comic model data to a listview.
 *
 * @author Daniel May
 * @version 2016-06-01.1
 */
class ComicArrayAdapter extends ArrayAdapter<Comic> {

    private final Context context;
    private final int layoutResourceId;
    private Comic[] data = null;

    /**
     * Initiates the adapter and saves the provided parameters
     *  @param context          Application Context
     * @param data             the comic array
     */
    public ComicArrayAdapter(Context context, Comic[] data) {
        super(context, R.layout.custom_comic_row, data);
        this.layoutResourceId = R.layout.custom_comic_row;
        this.context = context;
        this.data = data;
    }

    /**
     * Gets a single row view.
     *
     * @param position    the position in the array/listview
     * @param convertView the row's view
     * @param parent      the row's parent view
     * @return the row's view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ComicHolder holder;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
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

    /**
     * ViewHolder pattern for improved performance
     */
    static class ComicHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDetail;
    }
}