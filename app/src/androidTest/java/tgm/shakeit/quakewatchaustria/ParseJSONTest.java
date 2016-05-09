package tgm.shakeit.quakewatchaustria;

import android.test.AndroidTestCase;

import org.json.JSONObject;

/**
 * Test class for receiving JSON data.
 */
public class ParseJSONTest extends AndroidTestCase {
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
        JSONObject tmp = jp.getjObj();

        assertNotNull(jp.getjObj());
    }
}