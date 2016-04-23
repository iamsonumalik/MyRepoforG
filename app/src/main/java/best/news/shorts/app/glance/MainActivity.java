// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

// Referenced classes of package best.news.shorts.app.glance:
//            Controller, SavingCache, Storinggcm, LoadingActivity

public class MainActivity extends Activity
{
    private LinearLayout mainnews;
    private LinearLayout mainvideos;
    private boolean news;
    private RelativeLayout chooserlayout;
    private GridView gridview;
    private GridImages gridImages;
    private ArrayList<Drawable> image_clips;
    private RelativeLayout everythinglayout;
    private TextView time;

    private class AuthIt extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            sendJson("", "");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            chooserlayout.setVisibility(View.VISIBLE);
        }
    }


    private static final String MY_PREFS_NAME = "MySetting";
    private String deviceId;
    private File directory;
    private android.content.SharedPreferences.Editor editor;
    private boolean fromnoti;
    private String gettoken;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this);

        TextView tv = (TextView) findViewById(R.id.textView);
        mainnews = (LinearLayout) findViewById(R.id.mainnews);
        mainvideos = (LinearLayout) findViewById(R.id.mainvideos);
        chooserlayout = (RelativeLayout) findViewById(R.id.chooserlayout);
        everythinglayout = (RelativeLayout) findViewById(R.id.everythinglayout);
        time = (TextView) findViewById(R.id.time);
        gridview = (GridView) findViewById(R.id.loadinggridview);
        TextView videostv = (TextView) findViewById(R.id.videostv);
        TextView mainquestion = (TextView) findViewById(R.id.mainquestion);
        TextView newstv = (TextView) findViewById(R.id.newstv);

        gridImages = new GridImages(getBaseContext());
        image_clips = new ArrayList<Drawable>();
        image_clips = gridImages.getArrayBitmap();
        final ArrayList<Drawable> helper_clips = image_clips;
        final ImageAdapter im = new ImageAdapter(MainActivity.this, this, image_clips);
        gridview.setAdapter(im);

        Typeface greeting = Typeface.createFromAsset(getAssets(), "treench.otf");
        final Typeface face = Typeface.createFromAsset(getAssets(), "lodingfont.ttf");
        tv.setTypeface(face);
        time.setTypeface(greeting);
        mainquestion.setTypeface(greeting);
        newstv.setTypeface(face);
        videostv.setTypeface(face);

        Random r = new Random();
        Quotes quotes = new Quotes();
        time.setText(quotes.getQuoteslist(r.nextInt(12)));

        prefs = getSharedPreferences(MY_PREFS_NAME, 0);
        editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        Controller.getInstance().trackEvent("OpenApp", "Splash Screen", "user");

        if (Environment.getExternalStorageState() == null) {
            directory = new File(Environment.getDataDirectory()
                    + "/Android/data/best.news.shorts.app.glance/cache");
            File file = new File(directory, ".nomedia");

            if (!directory.exists()) {
                try {
                    directory.mkdirs();
                    FileOutputStream out = new FileOutputStream(file);
                    out.flush();
                    out.close();
                }catch (Exception e){
                    Log.e("Make Dir main",e.toString());
                }
                Log.e("Path",file.getAbsolutePath());
            }
        } else if (Environment.getExternalStorageState() != null) {
            directory = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/best.news.shorts.app.glance/cache");
            File file = new File(directory, ".nomedia");
            if (!directory.exists()) {
                try {
                    directory.mkdirs();
                    FileOutputStream out = new FileOutputStream(file);
                    out.flush();
                    out.close();
                }catch (Exception e){
                    Log.e("Make Dir Main",e.toString());
                }
                Log.e("Path", file.getAbsolutePath());
            }
        }// end of SD card checking

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        new AuthIt().execute();

        Intent i = getIntent();
        fromnoti = i.getBooleanExtra("fromnoti", false);
        if (fromnoti)
        {
            Controller.getInstance().trackEvent("DigestNews", "Clicked", "user");
        }

        mainnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LoadingActivity.class);
                intent.putExtra("fromnoti", fromnoti);
                intent.putExtra("news",true);
                startActivity(intent);

                finish();
            }
        });
        mainvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LoadingActivity.class);
                intent.putExtra("fromnoti", fromnoti);
                intent.putExtra("news",false);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void onPause()
    {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    protected void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    protected void sendJson(String s, String s1)
    {
        try {
            String url = getResources().getString(R.string.apiurl)+"/api/v1/user/authorize";
            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject cred = new JSONObject();
            cred.put("deviceId", deviceId);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(cred.toString());
            wr.flush();
//display what returns the POST request
            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println("bta" + sb.toString());
                JSONObject json_data = new JSONObject(String.valueOf(sb));
                gettoken=(json_data.getString("accessToken"));
                editor.putString("token", gettoken);
                editor.commit();
                editor.apply();
                new Storinggcm(MainActivity.this,gettoken);
            } else {
                System.out.println("else"+con.getResponseMessage());
            }
        }catch (Exception e){
            Log.e("Err",e.toString());
        }


        try
        {
            SavingCache savingCache = new SavingCache(this);
            savingCache.open();
            int i = savingCache.getTotal();
            Log.e("Total", String.valueOf(i));
            if (i > 99)
            {
                ArrayList<String> arraylist = savingCache.getAll();
                Log.e("Array Size", String.valueOf(arraylist.size()));
                while (arraylist.size() > 99)
                {
                    String s3 = arraylist.get(0);
                    String imageurl;
                    if (s3.contains(".png"))
                    {
                        imageurl = (new StringBuilder()).append("http://d2vwmcbs3lyudp.cloudfront.net/").append(s3).toString();
                    } else
                    {
                        imageurl = (new StringBuilder())
                                .append(getBaseContext()
                                        .getResources()
                                        .getString(R.string.url)).append(s3).append(".png").toString();
                    }
                    Picasso.with(getBaseContext()).invalidate(imageurl);
                    savingCache.deleteItem(s3);
                    arraylist.remove(0);
                    Log.e("Deleted", "cache");
                }
            }
            savingCache.close();

        }
        catch (Exception  e)
        {
            Log.e("Error inCaching", e.toString());
        }
            }

}
