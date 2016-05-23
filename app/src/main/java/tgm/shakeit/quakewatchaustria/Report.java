package tgm.shakeit.quakewatchaustria;

import android.location.Location;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Manages quakes to report.
 *
 * @author Daniel May
 * @version 2016-05-23.1
 */
public class Report {
    private static final String TAG = "Report.java";
    private static JSONObject addquestions = new JSONObject();
    private static String referenzID, locLon, locLat, locPrecision, locLastUpdate, mlocPLZ, mlocOrtsname, verspuert, kommentar, kontakt;
    private static int stockwerk, klassifikation;

    /**
     * Sets the fields affected by the location.
     *
     * @param toSet the given location to set
     */
    public static void setLocation(Location toSet) {
        if (toSet != null && toSet.hasAccuracy()) {
            DecimalFormat lonLat = new DecimalFormat("000.000000");
            locLon = lonLat.format(toSet.getLongitude());
            locLat = lonLat.format(toSet.getLatitude());
            locPrecision = new DecimalFormat("0000.00").format(toSet.getAccuracy());
            locLastUpdate = ISODateTimeFormat.dateTime().print(toSet.getTime());
        }
    }

    /**
     * Sets a reference quake id.
     *
     * @param reference the reference quake id.
     */
    public static void setReference(String reference) {
        referenzID = reference;
    }

    /**
     * Sets the mlocPLZ, only if the String contains 10 chars at max and only contains alphanumerical characters and hyphens.
     *
     * @param plz the PLZ to set.
     */
    public static void setPLZ(String plz) {
        if (plz.length() <= 10 && plz.matches("[a-zA-Z0-9\\-]+"))
            mlocPLZ = plz;
    }

    /**
     * Sets the location of an earthquake in textual form.
     *
     * @param ort the location
     */
    public static void setOrt(String ort) {
        mlocOrtsname = ort;
    }

    /**
     * Sets the location of an earthquake in textual form.
     *
     * @param floor the floor to set
     */
    public static void setFloor(int floor) {
        stockwerk = floor;
    }

    /**
     * Sets the felt magnitude.
     *
     * @param klass the felt magnitude
     */
    public static void setKlass(int klass) {
        klassifikation = klass;
    }

    /**
     * Sets the time, the quake was felt.
     *
     * @param stamp the time, the quake was recognized
     */
    public static void setTime(DateTime stamp) {
        verspuert = ISODateTimeFormat.dateTime().print(stamp);
    }

    /**
     * Sets the comment
     *
     * @param comment the comment to set
     */
    public static void setComment(String comment) {
        kommentar = comment;
    }

    /**
     * Sets the contact
     *
     * @param contact the contact to set
     */
    public static void setContect(String contact) {
        kontakt = contact;
    }

    /**
     * Adds an additional answer to the data
     *
     * @param question the question index
     * @param answer   the answer
     * @param zusatz   the answer if the index is 15
     */
    private static void addZusatz(int question, boolean answer, String zusatz) {
        if (zusatz == null) {
            DecimalFormat f = new DecimalFormat("00");
            try {
                addquestions = addquestions.put("f" + f.format(question), answer);
            } catch (JSONException e) {
                Log.e(TAG, "Data couldn't be added: " + e.getMessage());
            }
        } else {
            try {
                addquestions = addquestions.put("f15", zusatz);
            } catch (JSONException e) {
                Log.e(TAG, "Data couldn't be added: " + e.getMessage());
            }
        }
    }

    /**
     * Adds an additional answer to the data
     *
     * @param question the question index
     * @param answer   answer to the question
     */
    public static void addZusatz(int question, boolean answer) {
        addZusatz(question, answer, null);
    }

    /**
     * Adds an additional answer to the data
     *
     * @param zusatz the string for the 15th question
     */
    public static void addZusatz(String zusatz) {
        addZusatz(0, false, zusatz);
    }

    /**
     * Deletes every set value.
     */
    private static void clear() {
        referenzID = null;
        locLon = null;
        locLat = null;
        locPrecision = null;
        mlocPLZ = null;
        mlocOrtsname = null;
        stockwerk = 0;
        klassifikation = 0;
        verspuert = null;
        kommentar = null;
        kontakt = null;
        addquestions = new JSONObject();
    }

    /**
     * Generates a valid JSON object out of the gathered information
     *
     * @return the JSON Object to send to the ZAMG
     */
    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        try {
            result.put("referenzID", referenzID);
            result.put("locLon", locLon);
            result.put("locLat", locLat);
            result.put("locPrecision", locPrecision);
            result.put("mlocPLZ", mlocPLZ);
            result.put("mlocOrtsname", mlocOrtsname);
            result.put("stockwerk", stockwerk);
            result.put("klassifikation", klassifikation);
            result.put("verspuert", verspuert);
            result.put("kommentar", kommentar);
            result.put("kontakt", kontakt);
            result.put("addquestions", addquestions);
            clear();
            return result;
        } catch (JSONException e) {
            Log.e(TAG, "Data couldn't be gathered: " + e.getMessage());
            return null;
        }
    }
}
