// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import best.news.shorts.app.glance.gcm.GCMBaseIntentService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import org.json.JSONObject;

// Referenced classes of package best.news.shorts.app.glance:
//            SavingPublicId, SavingYoutubeLink, SavingViral, MainActivity, 
//            InsertPost, Controller, InsertUpdate

public class GCMIntentService extends GCMBaseIntentService
{

    private static final String MY_PREFS_NAME = "MySetting";
    private static final String TAG = "GCMIntentService";
    private Controller aController;

    public GCMIntentService()
    {
        super(new String[] {
            Config.GOOGLE_SENDER_ID
        });
        aController = null;
    }

    private void deletePost(String s)
    {
        try {
            SavingPublicId savingpublicid;
            SavingYoutubeLink savingyoutubelink;
            SavingViral savingviral;

            savingpublicid = new SavingPublicId(getBaseContext());
            savingyoutubelink = new SavingYoutubeLink(getBaseContext());
            savingviral = new SavingViral(getBaseContext());

            String s1;
            savingyoutubelink.open();
            savingpublicid.open();
            s1 = savingpublicid.getp_id(s);
            savingpublicid.deletePost(s);
            savingyoutubelink.deleteLink(s1);
            savingpublicid.close();
            savingyoutubelink.close();

            savingviral.open();
            savingviral.deletePost(s);
            savingviral.close();
            String imgageUrl;
            if (s1.contains(".png")){
                imgageUrl = "http://d2vwmcbs3lyudp.cloudfront.net/"+s1;

            }else {
                imgageUrl =getBaseContext().getResources().getString(R.string.url) + s1 + ".png";
            }

            Picasso.with(getBaseContext()).invalidate(imgageUrl);


            MyDirectory myDirectory = new MyDirectory();
            File directory = myDirectory.getDirectory();
            try {
                new File(directory, s1+".png").delete();
                Log.e("Deleted", s1);
            }catch (Exception e){

            }
        }catch ( Exception e){

        }
    }

    private void generateNotificationNews(Context context, String title,
                                          String body, String sound,
                                          String icon, String clickAction,
                                          String tag, String color) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromnoti",true);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.splash)
                // Set Ticker Message
                .setTicker(title)
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)

                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        builder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);

        remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.digest);
        remoteViews.setTextViewText(R.id.title,"");
        remoteViews.setTextViewText(R.id.text, body);
        remoteViews.setTextColor(R.id.title, Color.BLACK);
        remoteViews.setTextColor(R.id.text, Color.rgb(209,211,212));
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Random r = new Random();
        Notification notification = builder.build();
        int x = r.nextInt(1000000);
        notification.bigContentView = remoteViews;
        notificationmanager.notify(x,notification );
    }

    private void generateNotificationUpdate(Context context, String s, String s1, String s2)
    {
        deletePost(s2);
        if (s1.equals("UPDATE"))
        {
            (new InsertPost(s2, getBaseContext())).execute(new String[0]);
        }
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
        // notifies user

    }

    @Override
    public void onError(Context context, String errorId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    protected void onMessage(Context context, Intent intent) {
        if (aController == null)
            aController = (Controller) getApplicationContext();

        Log.e(TAG, "Received message");
        String title = intent.getExtras().getString("title");
        String body = intent.getExtras().getString("body");
        String sound = intent.getExtras().getString("sound");
        String clickAction = intent.getExtras().getString("clickAction");
        String icon = intent.getExtras().getString("icon");
        String tag = intent.getExtras().getString("tag");
        String color = intent.getExtras().getString("color");
        aController.displayMessageOnScreen(context, title);
        try {
            Log.e("Make Icon ", icon);
            JSONObject json_data = new JSONObject(icon);
            icon = json_data.getString("filename");
        } catch (Exception e) {
            Log.e("Make Icon ", e.toString());
        }

        try {
            Log.e("body", body);
            if (!clickAction.contains("")) {
                new InsertUpdate(clickAction, getBaseContext(), sound, title, body, icon).execute();
            } else {
                generateNotificationNews(context, title, body, sound, icon, clickAction, tag, color);
                try {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                String type = intent.getExtras().getString("type");
                String newsPost = intent.getExtras().getString("newsPost");
                generateNotificationUpdate(context, title, type, newsPost);
            } catch (Exception e2) {
                Log.e("Notification", e2.toString());
            }
        }

    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayMessageOnScreen(context, "Your device registred with GCM");
        //Log.d("NAME", GCMMainActivity.name);
        String accesstoken = "";
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, 0);

        accesstoken  = prefs.getString("token", "");
        aController.register(context, accesstoken, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if(aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));
        String accesstoken = "";


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, 0);

        accesstoken  = prefs.getString("token", "");

        aController.unregister(context, accesstoken, registrationId);
    }
}
