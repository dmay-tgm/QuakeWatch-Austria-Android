package tgm.shakeit.quakewatchaustria;

import android.test.AndroidTestCase;

import org.json.JSONException;

/**
 * Test class for receiving JSON data.
 *
 * @author Daniel May
 * @version 2016-05-11.1
 */
public class JSONLoaderTest extends AndroidTestCase {

    private JSONLoader jp;

    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        jp = null;
    }

    /**
     * test if the data could be received.
     */
    public void testWithSuccess() {
        jp = new JSONLoader(JSONLoader.AT);
        assertNotNull(jp.getjObj());
    }

    /**
     * tests if the process fails, if the URL isn't correct
     */
    public void testWithFail() {
        jp = new JSONLoader(JSONLoader.AT + "wrong");
        assertNull(jp.getjObj());
    }

    /**
     * tests if the process fails, if the URL is malformed
     */
    public void testMalformedURL() {
        jp = new JSONLoader("wrong");
        assertNull(jp.getjObj());
    }

    /**
     * tests if the array
     */
    public void testInterpretation() {
        jp = new JSONLoader(JSONLoader.AT);
        try {
            jp.getjObj().getJSONArray("features");
        } catch (JSONException e) {
            fail();
        }
    }
}