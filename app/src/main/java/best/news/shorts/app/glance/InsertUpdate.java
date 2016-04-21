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
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;

// Referenced classes of package best.news.shorts.app.glance:
//            SavingYoutubeLink, SavingViral, SavingPublicId, AllCategory

public class InsertUpdate extends  AsyncTask<String ,String,String>{

    private static final String MY_PREFS_NAME = "MySetting";
    private Context baseContext;
    private final String body;
    private final String breakit;
    private String fetchnew;
    private String icon;
    private InputStream is;
    private String line;
    private final String newsPost;
    private String result;
    private SavingPublicId savingPublicId;
    private SavingViral savingViral;
    private String title;
    private boolean isfirst;

    public InsertUpdate(String newsPost, Context baseContext, String s, String title, String body,String icon) {
        this.newsPost = newsPost;
        this.baseContext = baseContext;
        this.breakit = s;
        this.title = title;
        this.body = body;
        this.icon = icon;
    }

    protected void onPreExecute()
    {
        savingPublicId = new SavingPublicId(baseContext);
        try {
            savingPublicId.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = baseContext.getSharedPreferences(MY_PREFS_NAME, 0);
        isfirst = prefs.getBoolean("isfirst", false);
        if (!isfirst) {
            fetchnew = "-1";
        } else {

            String timest = savingPublicId.getLatetsTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateget = timest;
            Date date = null;
            try {
                date = sdf.parse(dateget);
                long st = date.getTime();
                fetchnew = String.valueOf(st);
            } catch (ParseException e) {
                fetchnew=timest;
                e.printStackTrace();
            }
        }
    }
    protected String doInBackground(String as[])
    {
        try {
            SharedPreferences prefs = baseContext.getSharedPreferences(MY_PREFS_NAME, 0);
            String gettoken = prefs.getString("token", "");
            String strUrl = baseContext.getResources().getString(R.string.apiurl) + "/api/v1/news/feed/" + fetchnew + "?apiKey=" + gettoken;
            strUrl = strUrl.replaceAll(" ", "%20");
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            Log.e("pass 1", "connection success ");
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");

            JSONObject json_data = new JSONObject(result);
            JSONArray array = json_data.getJSONArray("data");
            SavingYoutubeLink savingYoutubeLink;
            savingYoutubeLink = new SavingYoutubeLink(baseContext);
            savingYoutubeLink.open();

            //if (array.length()>0) {
            for (int i = 0; i < array.length(); i++) {
                //Log.e("L", String.valueOf(i));
                JSONObject data = array.getJSONObject(i);
                JSONObject publish = data.getJSONObject("publish");
                JSONObject portrait = publish.getJSONObject("portrait");
                JSONObject url_id = portrait.getJSONObject("url");
                JSONObject attributes = data.getJSONObject("attributes");
                JSONObject source = data.getJSONObject("source");
                JSONObject image = source.getJSONObject("image");
                JSONObject tags = data.getJSONObject("tags");
                JSONArray others = tags.getJSONArray("other");
                String othertags = "";
                for (int j = 0; j < others.length(); j++) {
                    String temptag = others.getString(j);
                    if (others.length() == 1) {
                        othertags += temptag;
                    } else if (j == 0) {
                        othertags += temptag;
                    } else if (j == (others.length() - 1)) {
                        othertags += "," + temptag;
                    } else {
                        othertags += "," + temptag;

                    }
                }
                JSONArray people = tags.getJSONArray("people");
                for (int j = 0; j < people.length(); j++) {
                    String temptag = people.getString(j);
                    othertags += "," + temptag;
                }
                JSONArray place = tags.getJSONArray("place");
                for (int j = 0; j < place.length(); j++) {
                    String temptag = place.getString(j);
                    othertags += "," + temptag;
                }
                //Log.e("Tags",othertags);
                //Temp Variables
                String _id = data.getString("_id");
                String p_id ="";
                String isS3;
                try{
                    p_id= url_id.getString("public_id");
                    isS3 = "false";
                }catch (Exception e){
                    p_id= url_id.getString("filename");
                    isS3 = "true";
                }
                String headline = data.getString("headline");
                String creditname = image.getString("name");
                String timestamp = data.getString("timestampCreated");
                String category = tags.getString("category");
                int editorRating = attributes.getInt("editorRating");
                String state = attributes.getString("state");
                String breakingNews = String.valueOf(attributes.getBoolean("breakingNews"));
                String enabled = String.valueOf(attributes.getBoolean("enabled"));
                String linkedToNews = "n";
                String issimplified = "false";
                String isFact = "false";
                String isviral = "false";
                try{
                    isFact = String.valueOf(tags.getBoolean("isFact"));
                }catch (Exception e){

                }
                try {
                    issimplified = String.valueOf(tags.getBoolean("isSimplified"));
                    isviral = String.valueOf(tags.getBoolean("isViral"));
                } catch (Exception e) {

                }
                try {
                    linkedToNews = data.getString("linkedToNews");
                } catch (Exception e) {

                }
                //Saving YouTubeLink
                try {
                    if (!(data.getString("youtubeVideoId").equals("") || data.getString("youtubeVideoId").equals(null))) {
                        String youtubeVideoId = data.getString("youtubeVideoId");
                        savingYoutubeLink.createEntry(1, p_id, youtubeVideoId);
                    }
                } catch (Exception e) {

                }

                //Saving Recent Posts
                if (!category.equals("VIRAL")) {
                    savingPublicId.createEntry(i,
                            _id,
                            p_id,
                            timestamp,
                            category,
                            issimplified,
                            isviral,
                            editorRating,
                            state,
                            breakingNews,
                            enabled,
                            othertags,
                            linkedToNews,
                            isFact,
                            headline,
                            isS3,
                            creditname
                    );
                }
                //Log.e("Save",headline);
            }
            savingYoutubeLink.close();

            if (breakit.equals("")){
                final RemoteViews remoteViews = new RemoteViews(baseContext.getPackageName(),
                        R.layout.customnotification);
                Intent intent = new Intent(baseContext, AllCategory.class);

                String pid="";

                pid = savingPublicId.getp_id(newsPost);

                intent.putExtra("moveto",pid);
                intent.putExtra("isnews",true);
                intent.putExtra("fromnoti",true);
                PendingIntent pIntent = PendingIntent.getActivity(baseContext, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(baseContext)
                        // Set Icon
                        .setSmallIcon(R.drawable.splash)
                        // Set Ticker Message
                        .setTicker(body)
                        // Dismiss Notification
                        .setAutoCancel(true)
                        // Set PendingIntent into Notification
                        .setContentIntent(pIntent)

                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                        // Set RemoteViews into Notification
                        .setContent(remoteViews);

                builder.setPriority(Notification.PRIORITY_HIGH);
                if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);
                if (!breakit.contains("breaking.wav"))
                    remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.digest);
                else
                    remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.breaking);

                remoteViews.setTextViewText(R.id.title, "");
                remoteViews.setTextViewText(R.id.text, body);
                remoteViews.setTextColor(R.id.title, Color.BLACK);
                remoteViews.setTextColor(R.id.text, Color.rgb(209,211,212));
                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) baseContext.getSystemService(baseContext.NOTIFICATION_SERVICE);
                Random r = new Random();
                Notification notification = builder.build();
                int x = r.nextInt(1000000);
                notification.bigContentView = remoteViews;
                notificationmanager.notify(x,notification );
            }
        }catch (Exception e){

        }

        return null;
    }



    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        try
        {
            if (!breakit.equals(""))
            {
                Vibrator v = (Vibrator) baseContext.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        }
        catch (Exception se)
        {
            se.printStackTrace();
        }
        try
        {
            savingPublicId.close();
            savingViral.close();
            return;
        } catch (Exception es)
        {
            es.printStackTrace();
        }
    }


}
