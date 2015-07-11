package audio.rabid.dev.skintherapy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Charles on 7/10/2015.
 *
 * My favorite helper methods.
 */
public class Utils {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Print byte array as a hex string
     *
     * @see <a href="http://stackoverflow.com/a/9855338/1539043">Source</a>
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * I got tired of writing {@link Toast#makeText} over and over. So the Toaster <code>makesToast</code> for you!
     *
     * @param a       Activity/Service displaying the message
     * @param message string to display
     */
    public static void Toaster(Context a, final String message) {
        if (a.getClass() == Activity.class) {
            final Activity activity = (Activity) a;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    Log.v(activity.getClass().getSimpleName(), message);
                }
            });
        } else {
            Toast.makeText(a.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Log.v(a.getClass().getSimpleName(), message);
        }

    }

    /**
     * Wrapper for sleep which ignores {@link InterruptedException}.
     *
     * @param ms milliseconds
     * @see Thread#sleep(long)
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace(); //oh well
        }
    }

    public static String limitLength(String in, int limit){
        if(in.length()<limit) {
            return in;
        }else{
            return in.substring(0, limit);
        }
    }

    private static String uniqueDeviceName = null;
    public static String getUniqueDeviceName(Context context){
        if(uniqueDeviceName == null){
            char[] androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).toCharArray();
            byte[] b = new byte[androidId.length];
            for(int i = 0; i< androidId.length; i++){
                b[i] = Byte.decode("0x"+androidId[i]);
            }
            uniqueDeviceName = Base64.encodeToString(b, Base64.DEFAULT).substring(0, 8);
        }
        return uniqueDeviceName;
    }

    private static final String PREFS_EXTRA = "APP_PREFS";
    private static final String NAME_EXTRA = "DRONE_NAME";
    public static String getMyName(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_EXTRA, Context.MODE_PRIVATE);
        return prefs.getString(NAME_EXTRA, getUniqueDeviceName(context));
    }

    public static boolean setMyName(Context context, String value){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_EXTRA, Context.MODE_PRIVATE);
        return prefs.edit().putString(NAME_EXTRA, value).commit();
    }


    /**
     * Send all the data from and InputStream into an OutputStream and then close them up.
     * @param is
     * @param os
     * @throws IOException
     */
    public static void pipe(InputStream is, OutputStream os) throws IOException{
        byte[] buffer = new byte[1024];
        int len = is.read(buffer);
        while (len != -1) {
            os.write(buffer, 0, len);
            len = is.read(buffer);
        }
        is.close();
        os.flush();
        os.close();
    }

    /**
     * Map an integer in one range to an integer in another. For example, -5 in range -10 to 10 maps to 25 in the range 0 to 100.
     * @param val
     * @param inLow
     * @param inHigh
     * @param outLow
     * @param outHigh
     * @return
     */
    public static int map(int val, int inLow, int inHigh, int outLow, int outHigh){
        if(val<inLow || val>inHigh) throw new IllegalArgumentException("value "+val+" outside of range "+inLow+" to "+inHigh);
        if(inLow>=inHigh) throw new IllegalArgumentException("Invalid range "+inLow+" to "+inHigh);
        if(outLow>=outHigh) throw new IllegalArgumentException("Invalid range "+outLow+" to "+outHigh);
        return Math.round(((float) (val - inLow) / (float) (inHigh - inLow)) * (outHigh - outLow) + outLow);
    }

    /**
     * Same as {@link #map(int, int, int, int, int)} except it takes a float between 0 and 1
     * @param val
     * @param outLow
     * @param outHigh
     * @return
     */
    public static int map(float val, int outLow, int outHigh){
        if(val<0 || val>1) throw new IllegalArgumentException(String.format("Value must be between 0 and 1 (got %f)",val));
        return outLow + Math.round(val * (float)(outHigh-outLow));
    }

    public static int byteArrayIndexOf(byte[] data, byte[] search){
        return byteArrayIndexOf(data, search, 0);
    }

    public static int byteArrayIndexOf(byte[] data, byte[] search, int index){
        int matches = 0;
        for(int i=index; i< data.length; i++){
            if(data[i] == search[matches]){
                matches++;
            }else if(matches>0){
                //this one isn't a match, but we should try again at this point from the beginning
                matches = 0;
                i--;
            }
            if(matches==search.length){
                return i - matches + 1;
            }
        }
        return -1;
    }

    public static void copyFile(File src, File dest) throws IOException{
        FileInputStream is = new FileInputStream(src);
        FileOutputStream os = new FileOutputStream(dest);
        pipe(is, os);
    }

    /**
     * Determine number of bytes in an InputStream (up to max 1GB)
     * @param r
     * @return
     */
    public static long getFileSize(InputStream r){
        int size = 0;
        try {
            r.mark(1024 * 1024 * 1024);
            while (r.read() >= 0) size++;
            r.reset();
            return size;
        }catch (IOException e){
            return -1;
        }
    }
    public static long getFileSize(File f){
        try {
            return getFileSize(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            return -1;
        }
    }

    public static void logInputStream(final String TAG, final InputStream is){
        int totalRead = 0;
        byte[] data = new byte[100];
        try {
            int bytesRead = is.read(data);
            while (bytesRead >= 0) {
                totalRead+=bytesRead;
                if(bytesRead>0) Log.d(TAG, "["+totalRead+"] "+new String(data).substring(0, bytesRead-1));
                bytesRead = is.read(data);
            }
        }catch (IOException e){
            Log.e(TAG, "Couldn't read InputStream", e);
        }
    }

    /**
     *
     * @param activity
     *
     * @see <a href="http://stackoverflow.com/questions/6599770/screen-orientation-lock">Source</a>
     */
    public static void disableRotation(Activity activity) {
        final int orientation = activity.getResources().getConfiguration().orientation;
        final int rotation = activity.getWindowManager().getDefaultDisplay().getOrientation();

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        }
    }

    public static void enableRotation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}
