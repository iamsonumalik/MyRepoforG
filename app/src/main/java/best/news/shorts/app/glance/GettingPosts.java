// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;

// Referenced classes of package best.news.shorts.app.glance:
//            MyDirectory, SavingYoutubeLink, SavingViral, SavingPublicId, 
//            AllCategory, OnBoarding

public class GettingPosts extends AsyncTask<String, String, Void> {

    private static final String MY_PREFS_NAME = "MySetting";
    private final Activity activity;
    private String fetchnew;
    private final boolean fromnoti;
    private String gettoken;
    private InputStream is;
    private boolean isfirst;
    private boolean isnews;
    private String line;
    private int news;
    private String result;
    boolean saveit;
    private SavingPublicId savingPublicId;
    private SavingViral savingViral;
    private final TextView totalnews;
    private int trends;

    public GettingPosts(Activity activity1, String s, TextView textview, boolean flag)
    {
        trends = 0;
        news = 0;
        isnews = true;
        saveit = true;
        activity = activity1;
        gettoken = s;
        totalnews = textview;
        fromnoti = flag;
    }


    protected void onPreExecute()
    {
        savingPublicId = new SavingPublicId(activity);
        try {
            savingPublicId.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, 0);
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

    private void saveImage(String name)
    {File directory;
        File file = null;
        MyDirectory myDirectory = new MyDirectory();
        directory = myDirectory.getDirectory();
        if (name.contains(".png"))
            file = new File(directory, name);
        else
            file = new File(directory, name + ".png");
        if (!file.exists()) {

            String imgageUrl ;
            String url;
            if (!(name.contains(".png")))
                imgageUrl = activity.getResources().getString(R.string.url) + name + ".png";
            else
                imgageUrl="http://d2vwmcbs3lyudp.cloudfront.net/"+name;
            try {
                InputStream in = new java.net.URL(imgageUrl).openStream();
                Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                FileOutputStream ourstream = new FileOutputStream(file);
                mIcon11.compress(Bitmap.CompressFormat.PNG, 100, ourstream);
                ourstream.flush();
                ourstream.close();
                Log.e("Saved","Loading");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                //Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {

            }
        }
    }


    @Override
    protected  Void doInBackground(String as[])
    {

        try {
            String strUrl = activity.getResources().getString(R.string.apiurl) + "/api/v1/news/feed/" + fetchnew + "?apiKey=" + gettoken;
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
            savingYoutubeLink = new SavingYoutubeLink(activity);
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

                if (i<1) {
                    //public_ids.add(i, p_id);
                    saveImage(p_id);
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
                if (issimplified.equals("true")||isviral.equals("true")){
                    trends++;
                }else {
                    news++;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (totalnews != null) {
                            int total = news + trends;
                            if (total==1)
                                totalnews.setText("There is "+String.valueOf(news+trends)+" new story.");
                            else if (total>1)
                                totalnews.setText("There are "+String.valueOf(news+trends)+" new stories.");
                            else
                                totalnews.setText("There are no new stories.");

                        }
                    }
                });
            }
            savingYoutubeLink.close();
        }catch (Exception e){

        }
        return null;
    }


    protected void onPostExecute(Void void1)
    {
        try
        {
            savingPublicId.close();
            savingViral.close();
        }
        // Misplaced declaration of an exception variable
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        if (isfirst)
        {
            Intent i = new Intent(activity, AllCategory.class);
            i.putExtra("moveto", "");
            i.putExtra("isnews", isnews);
            i.putExtra("fromnoti", fromnoti);
            activity.startActivity(i);
            activity.finish();
            return;
        } else
        {
            Intent i = new Intent(activity, OnBoarding.class);
            activity.startActivity(i);
            activity.finish();
            return;
        }
    }



}
