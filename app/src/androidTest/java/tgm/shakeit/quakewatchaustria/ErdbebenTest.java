package tgm.shakeit.quakewatchaustria;

import android.location.Location;
import android.test.AndroidTestCase;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONArray;
import org.json.JSONException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Test class for parsing JSON data to earthquake data.
 * Since this class is not testing the serialization of quakes, it's ok when a test fails with a NullPointerException (no connection).
 * These tests are intended to be reviewed at the Android Monitor. The tester can check the correct formatting of the strings.
 *
 * @author Daniel May
 * @version 2016-05-11.1
 */
public class ErdbebenTest extends AndroidTestCase {
    private JSONLoader jp;
    private Location fake;

    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
        jp = new JSONLoader(JSONLoader.AT);
        fake = new Location("fake");
        //TGM-Standort
        fake.setLatitude(48.236590);
        fake.setLongitude(16.369625);
        //initializing the library
        JodaTimeAndroid.init(getContext());
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        jp = null;
    }

    /**
     * test if the data could be interpreted.
     */
    public void testAt() throws JSONException {
        JSONArray tmp;
        tmp = jp.getjObj().getJSONArray("features");
        for (int i = 0; i < tmp.length(); i++) {
            Erdbeben quake = new Erdbeben(tmp.getJSONObject(i), fake);
            System.out.println("Magnitude: " + quake.getMag());
            System.out.println("Region: " + quake.getRegion());
            System.out.println("Ort: " + quake.getOrt());
            System.out.println("Datum: " + quake.getDate());
            System.out.println("Zeit: " + quake.getTime());
            System.out.println("Koordinaten: " + quake.getCords());
            System.out.println("Tiefe: " + quake.getDepth());
            System.out.println("Distanz: " + quake.getDistance());
            assertNotNull(quake.getDist());
            for (String abstand : quake.getDist()) {
                System.out.println("Entfernung: " + abstand);
                assertNotNull(abstand);
            }
            System.out.println("---------------------------");
            assertNotNull(quake.getMag());
            assertNotNull(quake.getRegion());
            assertNotNull(quake.getOrt());
            assertNotNull(quake.getDate());
            assertNotNull(quake.getTime());
            assertNotNull(quake.getCords());
            assertNotNull(quake.getDepth());
            assertNotNull(quake.getDistance());
        }
    }

    /**
     * test if the data could be interpreted.
     */
    public void testEu() throws JSONException {
        jp = new JSONLoader(JSONLoader.EU);
        JSONArray tmp;
        tmp = jp.getjObj().getJSONArray("features");
        for (int i = 0; i < tmp.length(); i++) {
            Erdbeben quake = new Erdbeben(tmp.getJSONObject(i), fake);
            System.out.println("Magnitude: " + quake.getMag());
            System.out.println("Region: " + quake.getRegion());
            System.out.println("Ort: " + quake.getOrt());
            System.out.println("Datum: " + quake.getDate());
            System.out.println("Zeit: " + quake.getTime());
            System.out.println("Koordinaten: " + quake.getCords());
            System.out.println("Tiefe: " + quake.getDepth());
            System.out.println("Distanz: " + quake.getDistance());
            assertNotNull(quake.getDist());
            for (String abstand : quake.getDist()) {
                System.out.println("Entfernung: " + abstand);
                assertNotNull(abstand);
            }
            System.out.println("---------------------------");
            assertNotNull(quake.getMag());
            assertNotNull(quake.getRegion());
            assertNotNull(quake.getOrt());
            assertNotNull(quake.getDate());
            assertNotNull(quake.getTime());
            assertNotNull(quake.getCords());
            assertNotNull(quake.getDepth());
            assertNotNull(quake.getDistance());
        }
    }

    /**
     * test if the data could be interpreted.
     */
    public void testWorld() throws JSONException {
        jp = new JSONLoader(JSONLoader.WORLD);
        JSONArray tmp;
        tmp = jp.getjObj().getJSONArray("features");
        for (int i = 0; i < tmp.length(); i++) {
            Erdbeben quake = new Erdbeben(tmp.getJSONObject(i), fake);
            System.out.println("Magnitude: " + quake.getMag());
            System.out.println("Region: " + quake.getRegion());
            System.out.println("Ort: " + quake.getOrt());
            System.out.println("Datum: " + quake.getDate());
            System.out.println("Zeit: " + quake.getTime());
            System.out.println("Koordinaten: " + quake.getCords());
            System.out.println("Tiefe: " + quake.getDepth());
            System.out.println("Distanz: " + quake.getDistance());
            assertNotNull(quake.getDist());
            for (String abstand : quake.getDist()) {
                System.out.println("Entfernung: " + abstand);
                assertNotNull(abstand);
            }
            System.out.println("---------------------------");
            assertNotNull(quake.getMag());
            assertNotNull(quake.getRegion());
            assertNotNull(quake.getOrt());
            assertNotNull(quake.getDate());
            assertNotNull(quake.getTime());
            assertNotNull(quake.getCords());
            assertNotNull(quake.getDepth());
            assertNotNull(quake.getDistance());
        }
    }

    /**
     * tests if the distance could be refreshed.
     */
    public void testRefreshDistance() throws JSONException {
        jp = new JSONLoader(JSONLoader.AT);
        JSONArray tmp;
        tmp = jp.getjObj().getJSONArray("features");
        Erdbeben quake = new Erdbeben(tmp.getJSONObject(0), fake);
        String before = quake.getDistance();
        Location newLoc = new Location("alsoFake");
        newLoc.setLongitude(32);
        newLoc.setLatitude(-12);
        quake.refreshDistanceFromQuake(newLoc);
        String after = quake.getDistance();
        assertThat(before, not(equalTo(after)));
    }

    /**
     * tests if the distance is null when the current location is also null.
     */
    public void testNullDistance() throws JSONException {
        jp = new JSONLoader(JSONLoader.AT);
        JSONArray tmp;
        tmp = jp.getjObj().getJSONArray("features");
        Erdbeben quake = new Erdbeben(tmp.getJSONObject(0), null);
        assertNull(quake.getDistance());
    }
}