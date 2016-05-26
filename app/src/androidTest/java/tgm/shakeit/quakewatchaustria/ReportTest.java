package tgm.shakeit.quakewatchaustria;

import android.location.Location;
import android.test.AndroidTestCase;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Test class for the quake report.
 *
 * @author Daniel May
 * @version 2016-05-26.1
 */
public class ReportTest extends AndroidTestCase {

    private static JSONObject addquestions = new JSONObject();

    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
        JodaTimeAndroid.init(getContext());
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        Report.clear();
    }

    /**
     * Tests if a location can be set.
     */
    public void testSetLocation() throws JSONException {
        Location fake = new Location("fake");
        //TGM-Standort
        fake.setLatitude(48.236590);
        fake.setLongitude(16.369625);
        fake.setAccuracy(20);
        fake.setTime(System.currentTimeMillis());
        Report.setLocation(fake);
        JSONObject result = Report.toJSON();
        assertEquals("048.236590", result.getString("locLat"));
        assertEquals("016.369625", result.getString("locLon"));
        assertEquals("0020.00", result.getString("locPrecision"));
        assertNotNull(ISODateTimeFormat.dateTimeParser().parseDateTime(result.getString("locLastUpdate")));
        System.out.println("Lat: " + result.getString("locLat"));
        System.out.println("Lon: " + result.getString("locLon"));
        System.out.println("Precision: " + result.getString("locPrecision"));
        System.out.println("LastUpdate: " + result.getString("locLastUpdate"));

    }

    /**
     * Test if the PLZ accepts only the right characters
     */
    public void testSetPLZ() throws JSONException {
        Report.setPLZ("A-2100");
        JSONObject result = Report.toJSON();
        assertEquals("A-2100", result.getString("mlocPLZ"));
        Report.clear();
        Report.setPLZ("12345678901");
        result = Report.toJSON();
        assertEquals("null", result.getString("mlocPLZ"));
        Report.clear();
        Report.setPLZ("jlk_^^");
        result = Report.toJSON();
        assertEquals("null", result.getString("mlocPLZ"));
    }

    /**
     * tests if the time could be set to an ISO-8601 String
     */
    public void testSetTime() throws JSONException {
        Report.setTime(new DateTime());
        JSONObject result = Report.toJSON();
        assertNotNull(ISODateTimeFormat.dateTimeParser().parseDateTime(result.getString("verspuert")));
        System.out.println("verspuert: " + result.getString("verspuert"));
    }

    /**
     * test if the additional questions could be set
     */
    public void testSetZusatz() throws JSONException {
        Report.addZusatz(1, true);
        JSONObject result = Report.toJSON();
        assertEquals(true, result.getJSONObject("addquestions").getBoolean("f01"));
        Report.addZusatz(14, false);
        result = Report.toJSON();
        assertEquals(false, result.getJSONObject("addquestions").getBoolean("f14"));
        Report.addZusatz("abcd");
        result = Report.toJSON();
        assertEquals("abcd", result.getJSONObject("addquestions").getString("f15"));
    }
}