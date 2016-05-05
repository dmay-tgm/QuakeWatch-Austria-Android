package tgm.shakeit.quakewatchaustria;

import java.util.ArrayList;

/**
 * Created by Moritz on 27.04.2016.
 */
public class Erdbeben {

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

    public Erdbeben (double mag, String reg, String ort, String date, String time, String cords,String depth,ArrayList<String> dist){
        this.mag=mag;
        this.region=reg;
        this.ort=ort;
        this.date=date;
        this.time=time;
        this.cords=cords;
        this.depth=depth;
        this.dist=dist;
    }



}
