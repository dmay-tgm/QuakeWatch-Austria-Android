package tgm.shakeit.quakewatchaustria;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Interprets the received JSON data and provides a definition for earthquakes. Is used for displaying an earthquakes.
 *
 * @author Daniel May
 * @version 2016-05-10.1
 */
public class Erdbeben {
    private static final String TAG = "Erdbeben.java";

    /**
     * Gets the magnitude in double format.
     *
     * @return magnitude
     */
    public double getMag() {
        return mag;
    }

    /**
     * Gets the earthquake's region.
     *
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getOrt() {
        return ort;
    }

    /**
     * Gets the earthquake's date in the user's local timezone.
     *
     * @return the formatted date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the earthquake's time in the user's local timezone.
     *
     * @return the formatted time
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the coordinates.
     *
     * @return the formatted coordinates
     */
    public String getCords() {
        return cords;
    }

    /**
     * Gets the depth of the earthquake.
     *
     * @return the formatted depth.
     */
    public String getDepth() {
        return depth;
    }

    /**
     * Gets the List of distances.
     *
     * @return list of distances to other places.
     */
    public ArrayList<String> getDist() {
        return dist;
    }

    private double mag; //Magnitude
    private String region; //Region: "Oberösterreich"
    private String ort; //Ort bzw. Stadt: "Linz"
    private String date; //Datum in Format: "1. Jan 2016"
    private String time; //Zeit in Format: "04:20"
    private String cords; //Koordinaten in Format: "47,34°N 14,54°O"
    private String depth; //Tiefe mit Einheit: "7,6 km"
    private ArrayList<String> dist; //Entfernungen zu anderen (relevanten) Orten: "3 km OSO von Wien"

    public Erdbeben(double mag, String reg, String ort, String date, String time/*, String cords,String depth,ArrayList<String> dist*/) {
        this.mag = mag;
        this.region = reg;
        this.ort = ort;
        this.date = date;
        this.time = time;
        //this.cords = cords;
        //this.depth = depth;
        //this.dist = dist;
    }

    /**
     * Parses a feature JSON object to a valid earthquake object.
     *
     * @param toParse feature JSON object
     */
    public Erdbeben(JSONObject toParse) {
        DecimalFormatSymbols decimalSymbol = new DecimalFormatSymbols();
        decimalSymbol.setDecimalSeparator(',');
        try {
            JSONObject properties = toParse.getJSONObject("properties");
            mag = properties.getDouble("mag");
            String location = properties.getString("region");
            if (location.contains("/")) {
                String[] splittedLocation = location.split("/");
                region = splittedLocation[1].trim();
                ort = splittedLocation[0].trim();
            } else {
                ort = "";
                region = location.trim();
            }
            DateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            Date date = input.parse(properties.getString(time));
            DateFormat outputTime = new SimpleDateFormat("HH:mm", Locale.GERMAN);
            outputTime.setTimeZone(TimeZone.getDefault());
            time = outputTime.format(date);
            DateFormat outputDate = new SimpleDateFormat("d. MMM yyyy", Locale.GERMAN);
            outputDate.setTimeZone(TimeZone.getDefault());
            this.date = outputDate.format(date);
            JSONArray coordinates = toParse.getJSONObject("geometry").getJSONArray("coordinates");
            double latitude = coordinates.getDouble(0);
            double longitude = coordinates.getDouble(1);
            String formattedLatitude, formattedLongitude;
            DecimalFormat cordFormat = new DecimalFormat("#0.0#", decimalSymbol);
            formattedLatitude = cordFormat.format(Math.abs(latitude)) + "°";
            formattedLongitude = cordFormat.format(Math.abs(longitude)) + "°";
            if (latitude > 0)
                formattedLatitude += "N ";
            else if (latitude < 0)
                formattedLatitude += "S ";
            else
                formattedLatitude += "N/S ";
            if (longitude > 0)
                formattedLongitude += "O";
            else if (latitude < 0)
                formattedLongitude += "W";
            else
                formattedLongitude += "O/W";
            cords = formattedLatitude + formattedLongitude;
            DecimalFormat depthFormat = new DecimalFormat("#0.0#", decimalSymbol);
            depth = depthFormat.format(properties.getDouble("depth")) + " km";
            JSONArray allPlaces = properties.getJSONArray("places");
            dist = new ArrayList<>();
            for (int i = 0; i < allPlaces.length(); i++)
                dist.add(allPlaces.getJSONObject(i).getString("text"));
        } catch (JSONException e) {
            Log.e(TAG, "Couldn't interpret the data: " + e.getMessage());
        } catch (ParseException e) {
            Log.e(TAG, "Wrong datetime format: " + e.toString());
        }
    }
}
