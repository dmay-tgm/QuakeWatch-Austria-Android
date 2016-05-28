package tgm.shakeit.quakewatchaustria;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Reads or writes generic data from/to a file using serialization.
 *
 * @author Daniel May
 * @version 2016-05-23.1
 */
public class FileManager<T> {
    public static final String EU_FILE = "eu.ser";
    public static final String AT_FILE = "at.ser";
    public static final String WORLD_FILE = "world.ser";
    public static final String API_KEY_FILE = "key.ser";
    public static final String REPORT_QUEUE_FILE = "queue.ser";
    public static final String LATEST_FILE = "latest.ser";

    private static final String TAG = "FileManager.java";

    /**
     * Saves the given generic content in a file in the internal storage.
     *
     * @param filename the filename to save
     * @param content  the generic content to save
     * @param context  the ApplicationContext to use
     */
    public void writeObject(String filename, T content, Context context) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(content);
            os.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Error writing data: " + e.toString());
        }
    }

    /**
     * Reads the content from a file and parses it to a generic object.
     *
     * @param filename the file to readObject from
     * @param context  the ApplicationContext to use
     */
    public T readObject(String filename, Context context) {
        T desiredObject = null;
        try {
            FileInputStream inputStream = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            desiredObject = (T) ois.readObject();
            ois.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File was not found: " + e.toString());
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Error reading data: " + e.toString());
        }
        return desiredObject;
    }
}