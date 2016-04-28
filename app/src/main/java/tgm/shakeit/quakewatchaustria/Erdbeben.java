package tgm.shakeit.quakewatchaustria;

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

    private double mag;
    private String region;
    private String ort;
    private String date;
    private String time;

    public Erdbeben (double mag, String reg, String ort, String date, String time){
        this.mag=mag;
        this.region=reg;
        this.ort=ort;
        this.date=date;
        this.time=time;
    }



}
