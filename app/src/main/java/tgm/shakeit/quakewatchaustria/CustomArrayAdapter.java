package tgm.shakeit.quakewatchaustria;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Usage:   Adapter for the ListViews to process data
 * sets the color Codes depending on magnitude
 *
 * @author Moritz Mühlehner
 * @version 2016-06-01.1
 */
public class CustomArrayAdapter extends ArrayAdapter<Erdbeben> {

    private final static String[] colorCodes = {
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
    //colorCodes depending on Magnitude
    private int lastPosition = -1;
    private List<Erdbeben> data;

    /**
     * Initializes the array adapter and saves the array.
     *
     * @param context  context
     * @param resource resource hand over for processing
     */
    public CustomArrayAdapter(Context context, ArrayList<Erdbeben> resource) {
        super(context, R.layout.customrow, resource);
        data = resource;
    }

    /**
     * Sets the data.
     *
     * @param temp the list of earthquakes
     */
    public void setData(List<Erdbeben> temp) {
        this.data = temp;
        notifyDataSetChanged();
    }

    /**
     * Override Method
     * Called When: This Method is called when the ViewPagerAdapter
     * generates the Fragments
     * And the single earthquake Objects are created and hand over as resource
     * Usage:       Calculates necessary Information about List Item
     *
     * @param position    position in ListView
     * @param convertView the View the ListView belongs to
     * @param parent      the parent ViewGroup
     * @return the row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creates The ViewHolder
        /**
         * The ViewHolder is used to provide much better
         * performance
         * Basically it holds the different items and positions til the next call
         */
        ViewHolder holder;
        //Inflate the Layout
        LayoutInflater inflater = LayoutInflater.from(getContext());

        /*
         * If the View is empty e.g the first call
         * We have to set the ViewHolder elements
         * The ViewHolder Elements need to be initialized
         * and defined
         */
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.customrow, parent, false);
            holder = new ViewHolder();
            holder.textMag = (TextView) convertView.findViewById(R.id.listText);
//            holder.region = (TextView) convertView.findViewById(R.id.textViewLocation);
            holder.time = (TextView) convertView.findViewById(R.id.textViewTime);
            holder.date = (TextView) convertView.findViewById(R.id.textViewDatum);
            holder.ort = (TextView) convertView.findViewById(R.id.textViewOrt);
            convertView.setTag(holder);
        } else
        /*
         * If its not the First Call
         * The old ViewHolder object has to be set
         * For better performance
         */ {
            holder = (ViewHolder) convertView.getTag();
        }
        /**
         * Set the holder properties and elements
         */
        Erdbeben temp = data.get(position);
        double mag = temp.getMag();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        holder.textMag.setText(new DecimalFormat("#0.0", dfs).format(mag));
        holder.date.setText(temp.getDate());
        holder.time.setText(temp.getTime());
        if (temp.getOrt() != null && !temp.getOrt().equals("")) {
            holder.region = (TextView) convertView.findViewById(R.id.textViewLocation);
            holder.region.setText(temp.getRegion());
            ((TextView) convertView.findViewById(R.id.textViewLocation2)).setText("");
            holder.ort.setText(temp.getOrt());
        } else {
            holder.region = (TextView) convertView.findViewById(R.id.textViewLocation2);
            holder.region.setText(temp.getRegion());
            ((TextView) convertView.findViewById(R.id.textViewLocation)).setText("");
            holder.ort.setText("");
        }
        /*
         ----------------------------------------------------
         ---            COLOR CODES COLOR CODES           ---
         ----------------------------------------------------
         1 		- 		1.4     -->	    3EA739
         1.5 	- 		1.9	    -->     338B2E		| Grün
         2 		- 		2.4	    -->	    296F25
         ----------------------------------------------------
         2.5	- 		2.9	    -->	    FBFE00
         3		- 		3.4	    -->	    D5D800		| Gelb
         3.5	-		3.9	    -->	    B1B300
         ----------------------------------------------------
         4		- 		4.4	    -->	    39508A
         4.5	- 		4.9	    -->	    2F4273		| Blau
         5		- 		5.4	    -->	    25355C
         ----------------------------------------------------
         5.5	- 		5.9	    -->	    FFA415
         6		- 		6.4	    -->	    FF9C00		| Orange
         6.5	- 		6.9	    -->	    E98F00
         ----------------------------------------------------
         7		- 		7.9	    -->	    D91283		| Lila
         8		- 		8.9	    -->	    BB006A
         ----------------------------------------------------
         9		- 		12		-->	    CA0000
         10		- 		12		-->	    CA0000		| Rot
         11		- 		12		-->	    CA0000
         12		- 		12		-->	    CA0000
         ----------------------------------------------------
         ----------------------------------------------------
         /**
         * This if divides by the magnitude which color to use
         */
        if ((mag >= 0) && (mag <= 2.49)) {
            if ((mag >= 0) && (mag <= 1.49))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[0]));
            if ((mag >= 1.50) && (mag <= 1.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[1]));
            if ((mag >= 2.00) && (mag <= 2.49))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[2]));
        } /*NEXT COLOR*/ else if ((mag >= 2.50) && (mag <= 3.99)) {
            if ((mag >= 2.50) && (mag <= 2.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[2]));
            if ((mag >= 3.0) && (mag <= 3.49))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[4]));
            if ((mag >= 3.5) && (mag <= 3.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[5]));
        }/*NEXT COLOR*/ else if ((mag >= 4) && (mag <= 5.49)) {
            if ((mag >= 4) && (mag <= 4.49))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[6]));
            if ((mag >= 4.5) && (mag <= 4.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[7]));
            if ((mag >= 5) && (mag <= 5.49))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[8]));
        }/*NEXT COLOR*/ else if ((mag >= 5.5) && (mag <= 6.99)) {
            if ((mag >= 5.5) && (mag <= 5.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[9]));
            if ((mag >= 6) && (mag <= 6.49))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[10]));
            if ((mag >= 6.5) && (mag <= 6.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[11]));
        }/*NEXT COLOR*/ else if ((mag >= 7) && (mag <= 8.99)) {
            if ((mag >= 7) && (mag <= 7.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[12]));
            if ((mag >= 8) && (mag <= 8.99))
                holder.textMag.setTextColor(Color.parseColor(colorCodes[14]));
        }/*NEXT COLOR*/ else if ((mag >= 9) && (mag <= 12))
            holder.textMag.setTextColor(Color.parseColor(colorCodes[14]));
        if ((position > lastPosition)) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
            animation.setDuration(300);
            //animation.setDuration(900);
            convertView.startAnimation(animation);
            lastPosition = position;
        }
        return convertView;
    }

    /**
     * View Holder class
     * inner class because it is simple and easier to use
     * in this particular case
     */
    static class ViewHolder {
        TextView textMag;
        TextView region;
        TextView time;
        TextView ort;
        TextView date;
    }
}