package tgm.shakeit.quakewatchaustria;

import android.content.Context;
import android.util.Log;

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
public class JSONSender {
    private static final String APIURL = "http://geoweb.zamg.ac.at/quakeapi/v02/getapikey";
    private static final String SENDURL = "http://geoweb.zamg.ac.at/quakeapi/v02/message";
    private static final String TAG = "JSONSender.java";
    private String APIkey = null;
    private LinkedList<Report> queue = null;

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
            apiHost = new URL(APIURL);
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
            String toSave = sb.toString();
            keySaver.writeObject(FileManager.API_KEY_FILE, toSave, context);
            APIkey = toSave;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Couldn't interpret the URL: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "Method is not supported: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Couldn't open the connection: " + e.getMessage());
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
        FileManager<LinkedList<Report>> queueLoader = new FileManager<>();
        queue = queueLoader.readObject(FileManager.API_KEY_FILE, context);
    }

    /**
     * Adds an earthquake to report to the queue and stores the queue.
     *
     * @param r       the earthquake to report
     * @param context given context
     */
    public void addToQueue(Report r, Context context) {
        queue.addLast(r);
        FileManager<LinkedList<Report>> fm = new FileManager<>();
        fm.writeObject(FileManager.REPORT_QUEUE_FILE, queue, context);
    }

    /**
     * Sends the queued reports to the server.
     *
     * @param r the report to send
     */
    private void sendSingle(Report r) {
        URL send;
        try {
            send = new URL(SENDURL);
            //configure the connection
            HttpURLConnection con = (HttpURLConnection) send.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Authorization", "Basic cXVha2VhcGk6I3FrcCZtbGRuZyM=");
            con.setRequestProperty("X-QuakeAPIKey", APIkey);
            con.setDoOutput(true);
            con.setChunkedStreamingMode(0);
            //write the data
            con.getOutputStream().write(r.toJSON().toString().getBytes("utf-8"));
            // read error message
            if (con.getResponseCode() != 200) {
                //read from URL
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
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
        for (Iterator<Report> iterator = queue.iterator(); iterator.hasNext(); ) {
            Report r = iterator.next();
            sendSingle(r);
            iterator.remove();
            FileManager<LinkedList<Report>> fm = new FileManager<>();
            fm.writeObject(FileManager.REPORT_QUEUE_FILE, queue, context);
        }
    }
}