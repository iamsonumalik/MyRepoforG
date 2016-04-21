// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

// Referenced classes of package best.news.shorts.app.glance:
//            Controller

public class CheckUpdate extends AsyncTask<String,Boolean,Boolean>{

    private final Activity activity;
    private String description;
    private boolean forceUpdate;
    private final String gettoken;
    private InputStream is;
    private String line;
    private String result;
    private String versionName;

    public CheckUpdate(String s, Activity activity1)
    {
        versionName = "";
        gettoken = s;
        activity = activity1;
        PackageManager manager = activity.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Update Checker 3", "Error parsing data " + e.toString());
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {

            String strUrl = activity.getResources().getString(R.string.apiurl) + "/api/v1/version/ANDROID/latest?apiKey=" + gettoken;
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
            description = json_data.getString("description");
            Log.e("Description" ,description);
            String version = json_data.getString("version");
            Log.e("Version" ,version);
            forceUpdate = json_data.getBoolean("forceUpdate");
            if (!versionName.equals(version)){
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    protected void onPostExecute(final Boolean b)
    {
        super.onPostExecute(b);
        if (b){
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Typeface head = Typeface.createFromAsset(activity.getAssets(), "lodingfont.ttf");
            View someLayout = inflater.inflate(R.layout.updatedialog, null);
            TextView des = (TextView) someLayout.findViewById(R.id.description);
            TextView later = (TextView) someLayout.findViewById(R.id.later);
            TextView dailogtitle = (TextView) someLayout.findViewById(R.id.dialogtitle);
            TextView update = (TextView) someLayout.findViewById(R.id.update);
            des.setText(description);

            dailogtitle.setTypeface(head);
            update.setTypeface(head);
            later.setTypeface(head);
            des.setTypeface(head);
            dialog.setView(someLayout);
            dialog.setCancelable(false);
            if (forceUpdate){
                later.setVisibility(View.GONE);
            }
            final AlertDialog alertDialog = dialog.create();
            update.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view1)
                {
                    alertDialog.dismiss();
                    Controller.getInstance().trackEvent("updateapp", "yes", "user");
                    final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            later.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    Controller.getInstance().trackEvent("updateapp", "no", "user");
                }
            });
            alertDialog.show();
        }
    }



}
