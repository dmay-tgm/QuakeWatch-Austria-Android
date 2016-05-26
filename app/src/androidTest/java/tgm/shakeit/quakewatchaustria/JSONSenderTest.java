package tgm.shakeit.quakewatchaustria;

import android.location.Location;
import android.test.AndroidTestCase;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Test class for sending JSON data.
 *
 * @author Daniel May
 * @version 2016-05-26.1
 */
public class JSONSenderTest extends AndroidTestCase {

    private JSONSender js;

    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
        JodaTimeAndroid.init(getContext());
        getContext().deleteFile(FileManager.API_KEY_FILE);
        getContext().deleteFile(FileManager.REPORT_QUEUE_FILE);
        js = new JSONSender(getContext());
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        js = null;
    }

    /**
     * tests if the API key is generated correctly
     */
    public void testAPIKey() {
        FileManager<String> fm = new FileManager<>();
        String api_key = fm.readObject(FileManager.API_KEY_FILE, getContext());
        assertNotNull(api_key);
        System.out.println(api_key);
    }

    /**
     * test if a report can be added to the queue
     */
    public void testAddToQueue() throws JSONException {
        Report.setReference("hjkafdsoiup");
        Location fake = new Location("fake");
        //TGM-Standort
        fake.setLatitude(48.236590);
        fake.setLongitude(16.369625);
        fake.setAccuracy(20);
        fake.setTime(System.currentTimeMillis());
        Report.setLocation(fake);
        Report.setPLZ("A-2100");
        Report.setOrt("Korneuburg");
        Report.setTime(new DateTime());
        Report.setComment("Testkommentar");
        Report.setContact("<Telefonnummer>");
        Report.setFloor(10);
        Report.setKlass(2);
        Report.addZusatz(1, true);
        Report.addZusatz(14, false);
        Report.addZusatz("Testzusatz");
        JSONObject r = Report.toJSON();
        js.addToQueue(r.toString(), getContext());
        FileManager<LinkedList<String>> fm = new FileManager<>();
        LinkedList<String> ll = fm.readObject(FileManager.REPORT_QUEUE_FILE, getContext());
        assertEquals(r.toString(), ll.getFirst());
    }

    /**
     * tests if a queued quake could be sent and is remove from the queue
     */
    public void testSendQueued() {
        Report.setReference("hjkafdsoiup");
        Location fake = new Location("fake");
        //TGM-Standort
        fake.setLatitude(48.236590);
        fake.setLongitude(16.369625);
        fake.setAccuracy(20);
        fake.setTime(System.currentTimeMillis());
        Report.setLocation(fake);
        Report.setPLZ("A-2100");
        Report.setOrt("Korneuburg");
        Report.setTime(new DateTime());
        Report.setComment("Testkommentar");
        Report.setContact("<Telefonnummer>");
        Report.setFloor(10);
        Report.setKlass(2);
        Report.addZusatz(1, true);
        Report.addZusatz(14, false);
        Report.addZusatz("Testzusatz");
        JSONObject r = Report.toJSON();
        js.addToQueue(r.toString(), getContext());
        js.sendQueued(getContext());
        FileManager<LinkedList<String>> fm = new FileManager<>();
        LinkedList<String> ll = fm.readObject(FileManager.REPORT_QUEUE_FILE, getContext());
        assertTrue(ll.isEmpty());
    }
}