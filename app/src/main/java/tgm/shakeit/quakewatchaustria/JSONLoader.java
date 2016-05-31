package tgm.shakeit.quakewatchaustria;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class for receiving the JSON data. Usage: first call the standard constructor of this class. Then call loadJSONFromURL with the preferred file. Then you can use the getter to receive the JSONObject.
 *
 * @author Daniel May
 * @version 2016-05-11.1
 */
public class JSONLoader {

    public static final String EU = "http://geoweb.zamg.ac.at/eq_app/eu_latest.json";
    public static final String AT = "http://geoweb.zamg.ac.at/eq_app/at_latest.json";
    public static final String WORLD = "http://geoweb.zamg.ac.at/eq_app/web_latest.json";
    public static final String LATEST = "http://geoweb.zamg.ac.at/eq_app/latest.json";
    private static final String TAG = "JSONLoader.java";
    private JSONObject jObj;

    /**
     * Loads JSON content from an URL and stores it in the JSONObject attribute. If the data is not available now, the attribute's value will be null.
     *
     * @param urlString the URL to load JSON data from. Must be one of EU, AT, WORLD, LATEST
     */
    public JSONLoader(String urlString) {
        jObj = null;
        URL url;
        URLConnection con;
        BufferedReader rURL;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlString);
            con = url.openConnection();
            rURL = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            while ((inputLine = rURL.readLine()) != null)
                sb.append(inputLine).append(System.getProperty("line.separator"));
            rURL.close();
            jObj = new JSONObject(sb.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "The URL is malformed: " + e.toString());
        } catch (IOException e) {
            //gui.makeToast(R.string.loading_error);
            System.err.println("Toast");
            Log.e(TAG, "Error receiving the data: " + e.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data: " + e.toString());
        }
    }

    /**
     * Gets the JSONObject
     *
     * @return the JSONObject
     */
    public JSONObject getjObj() {
        return jObj;
    }
}