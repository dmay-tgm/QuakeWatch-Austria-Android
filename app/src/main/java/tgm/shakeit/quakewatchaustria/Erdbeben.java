package tgm.shakeit.quakewatchaustria;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import  javax.xml.bind.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Moritz on 27.04.2016.
 */
public class Erdbeben {
    private static final String TAG = "Erdbeben.java";

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCords() {
        return cords;
    }

    public void setCords(String cords) {
        this.cords = cords;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public ArrayList<String> getDist() {
        return dist;
    }

    public void setDist(ArrayList<String> dist) {
        this.dist = dist;
    }

    private double mag; //Magnitude
    private String region; //Region: "Oberösterreich"
    private String ort; //Ort bzw. Stadt: "Linz"
    private String date; //Datum in Format: "1. Jan 2016"
    private String time; //Zeit in Format: "04:20"
    private String cords; //Koordinaten in Format: "47,34°N 14,54°O"
    private String depth; //Tiefe mit Einheit: "7,6 km"
    private ArrayList<String> dist; //Entfernungen zu anderen (relevanten) Orten: "3 km OSO von Wien"

    public Erdbeben (double mag, String reg, String ort, String date, String time/*, String cords,String depth,ArrayList<String> dist*/){
        this.mag=mag;
        this.region=reg;
        this.ort=ort;
        this.date=date;
        this.time=time;
        this.cords=cords;
        this.depth=depth;
        this.dist=dist;
    }
    public Erdbeben (JSONArray toParse, int index){
        try {
            JSONObject desiredObject=toParse.getJSONObject(index);
            JSONObject properties=desiredObject.getJSONObject("properties");
            mag=properties.getDouble("mag");
            String location=properties.getString("region");
            if(location.contains("/")) {
                String[] splittedLocation = location.split("/");
                region = splittedLocation[1].trim();
                ort = splittedLocation[0].trim();
            }else{
                ort="";
                region=location.trim();
            }
            DateFormat input = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSX");
            Date date = input.parse(properties.getString(time));
            DateFormat outputTime = new SimpleDateFormat("HH:mm",Locale.GERMAN);
            outputTime.setTimeZone(TimeZone.getDefault());
            time=outputTime.format(date);
            DateFormat outputDate = new SimpleDateFormat("d. MMM yyyy",Locale.GERMAN);
            outputDate.setTimeZone(TimeZone.getDefault());
            this.date=outputDate.format(date);
            JSONArray coordinates=desiredObject.getJSONObject("geometry").getJSONArray("coordinates");
            double latitude= coordinates.getDouble(0);
            double longitude= coordinates.getDouble(1);
            String formattedLatitude,formattedLongitude;
            //Koordinaten in Format: "47,34°N 14,54°O"
            //DecimalFormat decimalFormatter=new DecimalFormat("##.00",Locale.GERMAN);
            if(latitude>0){

            }

        } catch (JSONException e) {
            Log.e(TAG, "Couldn't interpret the data: " + e.toString());
        } catch (ParseException e) {
            Log.e(TAG, "Wrong datetime format: " + e.toString());
        }

    }
    private String longitutdeToString(double longitude){

    }

    private String latitudeToString(double latitude){

    }

}
