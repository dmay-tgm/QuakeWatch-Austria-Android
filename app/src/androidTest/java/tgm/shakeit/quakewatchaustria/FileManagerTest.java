package tgm.shakeit.quakewatchaustria;

import android.test.AndroidTestCase;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for the FileManager.
 *
 * @author Daniel May
 * @version 2016-05-11.1
 */
public class FileManagerTest extends AndroidTestCase {
    private String toSave = "test";
    private FileManager<String> fm;

    /**
     * sets up the tests
     */
    @Override
    public void setUp() {
        fm = new FileManager<>();
    }

    /**
     * tears down the tests
     */
    @Override
    public void tearDown() {
        getContext().deleteFile(FileManager.AT_FILE);
        fm = null;
    }

    /**
     * test if the received data-type is correct
     */
    @Test
    public void testReadType() {
        fm.writeObject(FileManager.AT_FILE, toSave, getContext());
        Object result = fm.readObject(FileManager.AT_FILE, getContext());
        assertThat(result, IsInstanceOf.instanceOf(String.class));
    }

    /**
     * test if the received data is null, when there is no file
     */
    @Test
    public void testReadNull() {
        Object result = fm.readObject(FileManager.AT_FILE, getContext());
        assertNull(result);
    }

    /**
     * test if the data could was successfully saved.
     */
    public void testWithSuccess() {
        fm.writeObject(FileManager.AT_FILE, toSave, getContext());
        Object result = fm.readObject(FileManager.AT_FILE, getContext());
        assertEquals(result, toSave);
    }

    /**
     * test if the data can be overridden.
     */
    public void testWithOverride() {
        fm.writeObject(FileManager.AT_FILE, "abc", getContext());
        fm.writeObject(FileManager.AT_FILE, toSave, getContext());
        Object result = fm.readObject(FileManager.AT_FILE, getContext());
        assertEquals(result, toSave);
    }
}