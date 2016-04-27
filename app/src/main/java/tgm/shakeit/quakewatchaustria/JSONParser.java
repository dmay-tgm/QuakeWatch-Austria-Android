package tgm.shakeit.quakewatchaustria;


import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class for receiving and parsing the JSON data. Usage: first call the standard constructor of this class. Then call loadJSONFromURL with the preferred file. Then you can use the getter to receive the JSONObject.
 *
 * @author Daniel May
 * @version 1.0
 */
public class JSONParser {

    private JSONObject jObj;
    public static final String EU = "http://geoweb.zamg.ac.at/eq_app/eu_latest.json";
    public static final String AT = "http://geoweb.zamg.ac.at/eq_app/at_latest.json";
    public static final String WORLD = "http://geoweb.zamg.ac.at/eq_app/latest.json";
    public static final String LATEST = "http://geoweb.zamg.ac.at/eq_app/web_latest.json";

    public static final String EU_FILE = "eu.txt";
    public static final String AT_FILE = "at.txt";
    public static final String WORLD_FILE = "world.txt";
    public static final String LATEST_FILE = "latest.txt";

    private static final String TAG = "JSONParser.java";

    /**
     * Loads JSON content from an URL and stores it in the JSONObject attribute. If the data is not available now, it will be fetched from saved files.
     *
     * @param urlString URL to use, must be one of EU, AT, WORLD, LATEST
     * @param context   the ApplicationContext to use
     * @param filename  the filename, if the data is fetched from a file
     */
    public void loadJSONFromURL(String urlString, Context context, String filename) {
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
            save(filename, sb.toString(), context);
        } catch (MalformedURLException e) {
            Log.e(TAG, "The URL is malformed: " + e.toString());
        } catch (IOException e) {
            read(filename, context);
            //gui.makeToast(R.string.loading_error);
            System.err.println("Toast");
            Log.e(TAG, "Error receiving the data: " + e.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data: " + e.toString());
        }
    }

    /**
     * Saves the given content in a file in the internal storage.
     *
     * @param filename the filename to save
     * @param content  the content to save
     * @param context  the ApplicationContext to use
     */
    private void save(String filename, String content, Context context) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Error writing data: " + e.toString());
        }
    }

    /**
     * Reads the content from a file and parses it to a JSONObject.
     *
     * @param filename the file to read from
     * @param context  the ApplicationContext to use
     */
    private void read(String filename, Context context) {
        String jsonString;
        try {
            InputStream inputStream = context.openFileInput(filename);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                jsonString = stringBuilder.toString();
                jObj = new JSONObject(jsonString);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File was not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Error reading data: " + e.toString());
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