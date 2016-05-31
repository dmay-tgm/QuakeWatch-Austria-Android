package tgm.shakeit.quakewatchaustria;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Interprets the received JSON data and provides a definition for the latest earthquakes.
 *
 * @author Daniel May
 * @version 2016-05-31.1
 */
public class LatestQuake implements Serializable {
    private static final String TAG = "LatestQuake.java";
    private double latitude, longitude;
    private String id, time;


    /**
     * Parses a feature JSON object to a valid earthquake object.
     *
     * @param toParse feature JSON object
     */
    public LatestQuake(JSONObject toParse) {
        try {
            id = toParse.getString("id");
            JSONArray coordinates = toParse.getJSONObject("geometry").getJSONArray("coordinates");
            latitude = coordinates.getDouble(0);
            longitude = coordinates.getDouble(1);

            JSONObject properties = toParse.getJSONObject("properties");
            //Parse ISO8601 String to a DateTime object with the user's local timezone
            DateTime date = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(properties.getString("time")).withZone(DateTimeZone.getDefault());
            //formatting the date
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("d. MMM HH:mm");
            this.time = dateTimeFormatter.print(date);
        } catch (JSONException e) {
            Log.e(TAG, "Couldn't interpret the data: " + e.getMessage());
        }
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }
}