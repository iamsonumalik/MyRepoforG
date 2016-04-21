package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sonu on 21/4/16.
 */
public class GettingVideos extends AsyncTask<String, String, Void> {

    private static final String MY_PREFS_NAME = "MySetting";
    private final Activity activity;
    private final TextView totalnews;
    private String gettoken;
    private SharedPreferences.Editor editor;

    public GettingVideos(Activity loadingActivity, String gettoken, TextView totalnews) {
        this.activity = loadingActivity;
        this.totalnews = totalnews;
        this.gettoken = gettoken;
    }

    protected void onPreExecute()
    {
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, 0);
        editor = activity.getSharedPreferences(MY_PREFS_NAME, 0).edit();
        //editor.putString("videosubcategory", "");
        //editor.commit();
        //editor.apply();

    }

    @Override
    protected  Void doInBackground(String as[])
    {

        try {
            String strUrl = activity.getResources().getString(R.string.apiurl) + "/api/v1/news/subcategory" + "?apiKey=" + gettoken;
            strUrl = strUrl.replaceAll(" ", "%20");
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Log.e("pass 1", "connection success ");
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();
            String result = sb.toString();
            Log.e("pass 2", "connection success ");

            JSONObject json_data = new JSONObject(result);
            JSONArray subCategory = json_data.getJSONArray("subCategory");

            //if (array.length()>0) {
            String subcat="";
            for (int i = 0; i < subCategory.length(); i++) {
                //Log.e("L", String.valueOf(i));
                String tempsubCategory = subCategory.getString(i);
                if (subCategory.length() == 1) {
                    subcat += tempsubCategory;
                } else if (i == 0) {
                    subcat += tempsubCategory;
                } else if (i == (subCategory.length() - 1)) {
                    subcat += "," + tempsubCategory;
                } else {
                    subcat += "," + tempsubCategory;
                }
            }
            editor.putString("videosubcategory", subcat);
            editor.commit();
            editor.apply();

        }catch (Exception e){

        }
        return null;
    }


    protected void onPostExecute(Void void1)
    {
            Intent i = new Intent(activity, Viral.class);
            activity.startActivity(i);
            activity.finish();

    }
}
