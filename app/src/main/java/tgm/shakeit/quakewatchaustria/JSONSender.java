package tgm.shakeit.quakewatchaustria;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Sends data to ZAMG server and manages the API key.
 *
 * @author Daniel May
 * @version 2016-05-23.1
 */
class JSONSender {
    private static final String API_URL = "http://geoweb.zamg.ac.at/quakeapi/v02/getapikey";
    private static final String SEND_URL = "http://geoweb.zamg.ac.at/quakeapi/v02/message";
    private static final String TAG = "JSONSender.java";
    private String APIkey = null;
    private LinkedList<String> queue = null;

    /**
     * Configures a new sender instance, that is ready to report earthquakes.
     * Checks if there is an API key and loads a new one, if necessary.
     * Should be instantiated, during the app start.
     *
     * @param context the given context
     */
    public JSONSender(Context context) {
        checkAPIkey(context);
        if (APIkey == null || APIkey.isEmpty())
            generateAPIkey(context);
        loadQueued(context);
        if (queue == null)
            queue = new LinkedList<>();
    }

    /**
     * Gets a new API key from the server and saves it permanently.
     *
     * @param context the given context
     */
    private void generateAPIkey(Context context) {
        URL apiHost;
        try {
            apiHost = new URL(API_URL);
            //configure the connection
            HttpURLConnection apiConnection = (HttpURLConnection) apiHost.openConnection();
            apiConnection.setRequestMethod("POST");
            apiConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            apiConnection.setRequestProperty("Authorization", "Basic cXVha2VhcGk6I3FrcCZtbGRuZyM=");
            //read from URL
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    apiConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine).append(System.getProperty("line.separator"));
            // cleaning up
            in.close();
            apiConnection.disconnect();
            // saving the key permanently
            FileManager<String> keySaver = new FileManager<>();
            String toSave = new JSONObject(sb.toString()).getString("apikey");
            keySaver.writeObject(FileManager.API_KEY_FILE, toSave, context);
            APIkey = toSave;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Couldn't interpret the URL: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "Method is not supported: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Couldn't open the connection: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Couldn't read the API key: " + e.getMessage());
        }
    }

    /**
     * Tries to load the API key from a file.
     *
     * @param context the given context
     */
    private void checkAPIkey(Context context) {
        FileManager<String> keyLoader = new FileManager<>();
        APIkey = keyLoader.readObject(FileManager.API_KEY_FILE, context);
    }

    /**
     * Tries to load queued earthquakes to report.
     *
     * @param context the given context
     */
    private void loadQueued(Context context) {
        FileManager<LinkedList<String>> queueLoader = new FileManager<>();
        queue = queueLoader.readObject(FileManager.REPORT_QUEUE_FILE, context);
    }

    /**
     * Adds an earthquake to report to the queue and stores the queue.
     *
     * @param r       the earthquake to report
     * @param context given context
     */
    public void addToQueue(String r, Context context) {
        queue.addLast(r);
        FileManager<LinkedList<String>> fm = new FileManager<>();
        fm.writeObject(FileManager.REPORT_QUEUE_FILE, queue, context);
    }

    /**
     * Sends the queued reports to the server.
     *
     * @param r the report to send
     */
    private void sendSingle(String r) {
        URL send;
        try {
            send = new URL(SEND_URL);
            //configure the connection
            HttpURLConnection con = (HttpURLConnection) send.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Authorization", "Basic cXVha2VhcGk6I3FrcCZtbGRuZyM=");
            con.setRequestProperty("X-QuakeAPIKey", APIkey);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);
            //write the data
            con.getOutputStream().write(r.getBytes("utf-8"));
            // read error message
            if (con.getResponseCode() != 200) {
                Log.e(TAG, "Error sending data: " + con.getResponseCode() + " " + con.getResponseMessage());
                //read from URL
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    sb.append(inputLine).append(System.getProperty("line.separator"));
                // cleaning up
                in.close();
                Log.e(TAG, "Error sending data: " + sb.toString());
            }
            con.disconnect();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Wrong encoding: " + e.getMessage());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Couldn't interpret the URL: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "Method is not supported: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Couldn't open the connection: " + e.getMessage());
        }
    }

    /**
     * Sends all queued reports.
     *
     * @param context given context
     */
    public void sendQueued(Context context) {
        for (Iterator<String> iterator = queue.iterator(); iterator.hasNext(); ) {
            String r = iterator.next();
            sendSingle(r);
            iterator.remove();
            FileManager<LinkedList<String>> fm = new FileManager<>();
            fm.writeObject(FileManager.REPORT_QUEUE_FILE, queue, context);
        }
    }
}