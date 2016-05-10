package tgm.shakeit.quakewatchaustria;

import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Test class for receiving JSON data.
 */
public class ParseJSONTest extends AndroidTestCase {
    private JSONParser jp;
    private static final String TAG = "JSONParser.java";
    private double mag; //Magnitude
    private String region; //Region: "Oberösterreich"
    private String ort; //Ort bzw. Stadt: "Linz"
    private String date; //Datum in Format: "1. Jan 2016"
    private String time; //Zeit in Format: "04:20"
    private String cords; //Koordinaten in Format: "47,34°N 14,54°O"
    private String depth; //Tiefe mit Einheit: "7,6 km"
    private ArrayList<String> dist;

    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
        jp = new JSONParser();
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        getContext().deleteFile(JSONParser.AT_FILE);
        getContext().deleteFile(JSONParser.EU_FILE);
        jp = null;
    }

    /**
     * test if the data could be received.
     */
    public void testWithSuccess() {
        jp.loadJSONFromURL(JSONParser.AT, getContext(), JSONParser.AT_FILE);
        JSONArray tmp = null;
        try {
            tmp = jp.getjObj().getJSONArray("features");
            for (int i = 0; i < tmp.length(); i++) {
                Erdbeben quake = new Erdbeben(tmp.getJSONObject(i));
                System.out.println("Magnitude: " + quake.getMag());
                System.out.println("Region: " + quake.getRegion());
                System.out.println("Ort: " + quake.getOrt());
                System.out.println("Datum: " + quake.getDate());
                System.out.println("Zeit: " + quake.getTime());
                System.out.println("Koordinaten: " + quake.getCords());
                System.out.println("Tiefe: " + quake.getDepth());
                assertNotNull(quake.getDist());
                for (String abstand : quake.getDist()) {
                    System.out.println("Entfernung: " + abstand);
                    assertNotNull(abstand);
                }
                System.out.println();
                assertNotNull(quake.getMag());
                assertNotNull(quake.getRegion());
                assertNotNull(quake.getOrt());
                assertNotNull(quake.getDate());
                assertNotNull(quake.getTime());
                assertNotNull(quake.getCords());
                assertNotNull(quake.getDepth());
            }
        } catch (JSONException e) {
            Log.e(TAG, "Couldn't interpret the data: " + e.toString());
        }
    }
}