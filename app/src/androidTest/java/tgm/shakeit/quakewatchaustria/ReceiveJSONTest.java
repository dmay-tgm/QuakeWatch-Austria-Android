package tgm.shakeit.quakewatchaustria;

import android.test.AndroidTestCase;

/**
 * Test class for receiving JSON data.
 */
public class ReceiveJSONTest extends AndroidTestCase {
    private JSONParser jp;
    private static final String TAG = "JSONParser.java";


    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
        jp = new JSONParser();
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        getContext().deleteFile(JSONParser.AT_FILE);
        getContext().deleteFile(JSONParser.EU_FILE);
        jp = null;
    }

    /**
     * test if the data could be received.
     */
    public void testWithSuccess() {
        jp.loadJSONFromURL(JSONParser.AT, getContext(), JSONParser.AT_FILE);
        assertNotNull(jp.getjObj());
    }

    /**
     * tests if the process fails, if the URL isn't correct and the provided file is empty
     */
    public void testWithFail() {
        jp.loadJSONFromURL("http://abc/c", getContext(), JSONParser.EU_FILE);
        assertNull(jp.getjObj());
    }

    /**
     * tests if something is loaded from the file, because the URL isn't correct
     */
    public void testWithBackup() {
        jp.loadJSONFromURL(JSONParser.AT, getContext(), JSONParser.AT_FILE);
        jp = null;
        setUp();
        jp.loadJSONFromURL("http://abc/c", getContext(), JSONParser.AT_FILE);
        assertNotNull(jp.getjObj());
    }

    /**
     * tests if the downloaded JSON is the same as, later provided by the file.
     */
    public void testFileRead() {
        jp.loadJSONFromURL(JSONParser.AT, getContext(), JSONParser.AT_FILE);
        String tmp = jp.getjObj().toString();
        jp.loadJSONFromURL(JSONParser.EU, getContext(), JSONParser.EU_FILE);
        jp.loadJSONFromURL("http://abc/c", getContext(), JSONParser.AT_FILE);
        assertEquals(tmp, jp.getjObj().toString());
    }
}