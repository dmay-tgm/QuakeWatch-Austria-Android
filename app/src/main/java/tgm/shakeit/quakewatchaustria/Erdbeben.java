package tgm.shakeit.quakewatchaustria;

import android.location.Location;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

/**
 * Interprets the received JSON data and provides a definition for earthquakes. Is optimized for displaying an earthquakes.
 *
 * @author Daniel May
 * @version 2016-05-11.1
 */
public class Erdbeben implements Serializable {
    private static final String TAG = "Erdbeben.java";
    private final DecimalFormatSymbols decimalSymbol;
    /**
     * will be null if the given current location is also null
     */
    private String distance;// "7,0 km"
    private double mag; //Magnitude
    private String region; //Region: "Oberösterreich"
    private String ort; //Ort bzw. Stadt: "Linz"
    private String date; //Datum in Format: "1. Jan 2016"
    private String time; //Zeit in Format: "04:20"
    private String cords; //Koordinaten in Format: "47,34°N 14,54°O"
    private String depth; //Tiefe mit Einheit: "7 km"
    private ArrayList<String> dist; //Entfernungen zu anderen (relevanten) Orten: "3 km OSO von Wien"


    /**
     * Parses a feature JSON object to a valid earthquake object. If the user's GPS is not enabled, just pass null to the constructor and print out an error message.
     *
     * @param toParse feature JSON object
     * @param current current geolocation of the user
     */
    public Erdbeben(JSONObject toParse, Location current) {
        //german-like formatting
        decimalSymbol = new DecimalFormatSymbols();
        decimalSymbol.setDecimalSeparator(',');
        decimalSymbol.setGroupingSeparator('.');
        try {
            JSONObject properties = toParse.getJSONObject("properties");
            mag = properties.getDouble("mag");
            String location = properties.getString("region");
            //split the region into a city and a real region if it contains the delimiter
            if (location.contains("/")) {
                String[] splittedLocation = location.split("/");
                region = splittedLocation[1].trim();
                ort = splittedLocation[0].trim();
            } else {
                ort = "";
                region = location.trim();
            }
            //Parse ISO8601 String to a DateTime object with the user's local timezone
            DateTime date = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(properties.getString("time")).withZone(DateTimeZone.getDefault());
            //formatting the date
            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("d. MMM yyyy");
            this.date = dateFormatter.print(date);
            //formatting the time
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
            this.time = timeFormatter.print(date);
            //get the coordinates
            JSONArray coordinates = toParse.getJSONObject("geometry").getJSONArray("coordinates");
            double latitude = coordinates.getDouble(0);
            double longitude = coordinates.getDouble(1);
            //formatting of a coordinate
            DecimalFormat cordFormat = new DecimalFormat("0.0#", decimalSymbol);
            StringBuilder sb = new StringBuilder();
            sb.append(cordFormat.format(Math.abs(latitude))).append("°");
            //append the correct direction
            if (latitude > 0)
                sb.append("N ");
            else if (latitude < 0)
                sb.append("S ");
            else
                sb.append("N/S ");
            sb.append(cordFormat.format(Math.abs(longitude))).append("°");
            if (longitude > 0)
                sb.append("O");
            else if (latitude < 0)
                sb.append("W");
            else
                sb.append("O/W");
            cords = sb.toString();
            //rounding the depth
            depth = Math.round(properties.getDouble("depth")) + " km";
            //parse near places
            JSONArray allPlaces = properties.getJSONArray("places");
            dist = new ArrayList<>();
            for (int i = 0; i < allPlaces.length(); i++)
                dist.add(allPlaces.getJSONObject(i).getString("text"));
            //creating Location object for the earthquake
            Location quake = new Location("byLatLon");
            quake.setLatitude(latitude);
            quake.setLongitude(longitude);
            //save the distance to the quake
            refreshDistanceFromQuake(current, quake);
        } catch (JSONException e) {
            Log.e(TAG, "Couldn't interpret the data: " + e.getMessage());
        }
    }

    /**
     * Calculates the distance from an earthquake's epicentre to the user's mobile phone.
     *
     * @param phone the mobile phone's current location
     */
    public void refreshDistanceFromQuake(Location phone, Location quake) {
        distance = null;
        if (phone != null) {
            double tmp = phone.distanceTo(quake);
            tmp /= 1000.0;
            DecimalFormat distFormat = new DecimalFormat(",##0.0", decimalSymbol);
            distance = distFormat.format(tmp) + " km";
        }
    }

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
     * Gets the user's distance from an earthquake.
     *
     * @return the formatted distance.
     */
    public String getDistance() {
        return distance;
    }

    /**
     * Gets the List of distances.
     *
     * @return list of distances to other places.
     */
    public ArrayList<String> getDist() {
        return dist;
    }
}