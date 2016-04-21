package best.news.shorts.app.glance;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.util.Log;
import best.news.shorts.app.glance.gcm.GCMRegistrar;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
// Referenced classes of package best.news.shorts.app.glance:
//            AnalyticsTrackers

public class Controller extends Application
{

    private final int MAX_ATTEMPTS = 5;
    private final int BACKOFF_MILLI_SECONDS = 2000;
    private final Random random = new Random();
    static String nameget;
    static String emailget;
    public static final String TAG = Controller.class.getSimpleName();
    private static Controller mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }
    public static synchronized Controller getInstance() {
        return mInstance;
    }
    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }
    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
// Set screen name.
        t.setScreenName(screenName);
// Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }
    /***
     * Tracking event
     *
     * @param category event category
     * @param action action of the event
     * @param label label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
// Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
    // Register this account with the server.
    void register(final Context context, String accesstoken, final String regId) {
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
// Once GCM returns a registration id, we need to register on our server
// As the server might be down, we will retry it a couple
// times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(Config.TAG, "Attempt #" + i + " to register");
            try {
//Send Broadcast to Show message on screen
                displayMessageOnScreen(context, context.getString(
                        R.string.server_registering, 0, MAX_ATTEMPTS));
// Post registration values to web server
                post(regId, accesstoken);
                GCMRegistrar.setRegisteredOnServer(context, true);
//Send Broadcast to Show message on screen
                String message = context.getString(R.string.server_registered);
                displayMessageOnScreen(context, message);
                return;
            } catch (IOException e) {
// Here we are simplifying and retrying on any error; in a real
// application, it should retry only on unrecoverable errors
// (like HTTP error code 503).
                Log.e(Config.TAG, "Failed to register on attempt " + i + ":" + e);
//if (i == MAX_ATTEMPTS) {
// break;
//}
                try {
                    Log.d(Config.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
// Activity finished before we complete - exit.
                    Log.d(Config.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
// increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
//Send Broadcast to Show message on screen
        displayMessageOnScreen(context, message);
    }
    // Unregister this account/device pair within the server.
    void unregister(final Context context,String accesstoken, final String regId) {
        Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = Config.YOUR_SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(regId, accesstoken);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            displayMessageOnScreen(context, message);
        } catch (IOException e) {
// At this point the device is unregistered from GCM, but still
// registered in the our server.
// We could try to unregister again, but it is not necessary:
// if the server tries to send a message to the device, it will get
// a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            displayMessageOnScreen(context, message);
        }
    }
    // Issue a POST request to the server.
    private void post(String endpoint, String acest)
            throws IOException {
        String strUrl = getResources().getString(R.string.apiurl)+"/api/v1/User/gcmToken/"+endpoint+"?apiKey="+acest;
        strUrl = strUrl.replaceAll(" ","%20");
        URL url = new URL(strUrl);
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        InputStream is = urlConnection.getInputStream();
    }
    // Checking for all possible internet providers
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity =
                (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }
    // Notifies UI to display a message.
    void displayMessageOnScreen(Context context, String message) {
        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
// Send Broadcast to Broadcast receiver with message
        context.sendBroadcast(intent);
    }
    //Function to display simple Alert Dialog
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
// Set Dialog Title
        alertDialog.setTitle(title);
// Set Dialog Message
        alertDialog.setMessage(message);
        if(status != null)
// Set alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
// Set OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
// Show Alert Message
        alertDialog.show();
    }
    private PowerManager.WakeLock wakeLock;
    public void acquireWakeLock(Context context) {
        if (wakeLock != null) wakeLock.release();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
        wakeLock.acquire();
    }
    public void releaseWakeLock() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}

