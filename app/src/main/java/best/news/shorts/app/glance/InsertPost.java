// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

// Referenced classes of package best.news.shorts.app.glance:
//            SavingYoutubeLink, SavingViral, SavingPublicId

public class InsertPost extends AsyncTask<String ,String,String>{


    private static final String MY_PREFS_NAME = "MySetting";
    private Context baseContext;
    private String icon;
    private InputStream is;
    private String line;
    private final String newsPost;
    private String result;
    private SavingPublicId savingPublicId;
    private SavingViral savingViral;
    private String title;

    public InsertPost(String s, Context context)
    {
        newsPost = s;
        baseContext = context;
    }




    protected void onPreExecute()
    {
        super.onPreExecute();
        savingPublicId = new SavingPublicId(baseContext);
        savingViral = new SavingViral(baseContext);
        try
        {
            savingPublicId.open();
            savingViral.open();
        } catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected String doInBackground(String as[])
    {
        try {

        SharedPreferences prefs = baseContext.getSharedPreferences(MY_PREFS_NAME, 0);
        String gettoken = prefs.getString("token", "");
        String strUrl = baseContext.getResources().getString(R.string.apiurl)
                +"/api/v1/news/post/" + newsPost + "?apiKey=" + gettoken;
        strUrl = strUrl.replaceAll(" ", "%20");
        URL url = new URL(strUrl);
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        is = urlConnection.getInputStream();
        Log.e("pass 1", "connection success ");
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");

        }
        is.close();
        result = sb.toString();
        Log.e("pass 2", "connection success ");
            JSONObject json_data = new JSONObject(result);
            SavingYoutubeLink savingYoutubeLink;
            savingYoutubeLink = new SavingYoutubeLink(baseContext);
            savingYoutubeLink.open();
            JSONObject publish = json_data.getJSONObject("publish");
            JSONObject portrait = publish.getJSONObject("portrait");
            JSONObject url_id = portrait.getJSONObject("url");
            JSONObject attributes = json_data.getJSONObject("attributes");
            JSONObject source = json_data.getJSONObject("source");
            JSONObject image = source.getJSONObject("image");
            JSONObject tags = json_data.getJSONObject("tags");
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
                    othertags += ", " + temptag;

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
            //Temp Variables
            String _id = json_data.getString("_id");
            String p_id ="";
            String isS3;
            try{
                p_id= url_id.getString("public_id");
                isS3 = "false";
            }catch (Exception e){
                p_id= url_id.getString("filename");
                isS3 = "true";
            }
            String headline = json_data.getString("headline");
            String creditname = image.getString("name");
            String timestamp = json_data.getString("timestampCreated");
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
                linkedToNews = json_data.getString("linkedToNews");
            } catch (Exception e) {

            }
            //Saving YouTubeLink
            try {
                if (!(json_data.getString("youtubeVideoId").equals("") || json_data.getString("youtubeVideoId").equals(null))) {
                    String youtubeVideoId = json_data.getString("youtubeVideoId");
                    savingYoutubeLink.createEntry(1, p_id, youtubeVideoId);
                }
            } catch (Exception e) {

            }

            //Saving Recent Posts
            if (!category.equals("VIRAL")) {
                savingPublicId.createEntry(0,
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
                        headline, isS3, creditname);
            }
            Log.e("Updated",newsPost);
            savingYoutubeLink.close();
        }catch (Exception e){

        }
        return null;
    }


    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        try
        {
            savingPublicId.close();
            savingViral.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
