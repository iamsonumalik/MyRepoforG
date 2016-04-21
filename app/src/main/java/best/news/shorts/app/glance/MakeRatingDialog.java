package best.news.shorts.app.glance;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by malik on 27/2/16.
 */
public class MakeRatingDialog{
    private final Activity getactivity;
    private final SharedPreferences prefs;
    private String feedbacktext;
    private static final String MY_PREFS_NAME = "MySetting";

    public MakeRatingDialog(final Activity allCategory, final SharedPreferences.Editor editor, boolean didyes) {
        this.getactivity = allCategory;
        prefs = getactivity.getSharedPreferences(MY_PREFS_NAME, 0);

        if (didyes){
            rateUs(allCategory,editor);
        }else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(allCategory);
            LayoutInflater inflater = (LayoutInflater) allCategory.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Typeface head = Typeface.createFromAsset(allCategory.getAssets(), "lodingfont.ttf");
            View someLayout = inflater.inflate(R.layout.satisfactorydailog, null);
            TextView des = (TextView) someLayout.findViewById(R.id.satisfydescription);
            TextView no = (TextView) someLayout.findViewById(R.id.no);
            TextView dailogtitle = (TextView) someLayout.findViewById(R.id.satisfytitle);
            TextView yes = (TextView) someLayout.findViewById(R.id.yes);
            dialog.setCancelable(false);
            dailogtitle.setTypeface(head);
            yes.setTypeface(head);
            no.setTypeface(head);
            des.setTypeface(head);
            dialog.setView(someLayout);

            final AlertDialog alertDialog = dialog.create();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Controller.getInstance().trackEvent("Like the App", "YES", "user");
                    editor.putBoolean("didyes", true);
                    editor.commit();
                    rateUs(allCategory, editor);
                    alertDialog.dismiss();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Controller.getInstance().trackEvent("DisLike the App", "NO", "user");
                    editor.putBoolean("israted", true);
                    editor.commit();
                    alertDialog.dismiss();
                    sumbitFeedback(allCategory,editor);
                }
            });


            alertDialog.show();
        }

    }

    private void sumbitFeedback(Activity allCategory, SharedPreferences.Editor editor) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(allCategory);
        LayoutInflater inflater = (LayoutInflater) allCategory.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Typeface head = Typeface.createFromAsset(allCategory.getAssets(), "lodingfont.ttf");
        View someLayout = inflater.inflate(R.layout.sumbitdialog, null);
        TextView submittitle = (TextView) someLayout.findViewById(R.id.submittitle);
        TextView submitdes = (TextView) someLayout.findViewById(R.id.submitdes);
        final EditText feed = (EditText) someLayout.findViewById(R.id.feed);
        TextView submitfeedback = (TextView) someLayout.findViewById(R.id.submitfeedback);

        feed.setTypeface(head);
        submitfeedback.setTypeface(head);
        submitdes.setTypeface(head);
        submittitle.setTypeface(head);
        dialog.setView(someLayout);

        final AlertDialog alertDialog = dialog.create();
        submitfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbacktext = feed.getText().toString();
                if (feedbacktext.equals("")||feedbacktext.equals(null)){
                    feed.setBackgroundColor(Color.RED);
                }else {
                    getactivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SendFeedBack().execute();
                        }
                    });
                    alertDialog.dismiss();
                }
            }

        });



        alertDialog.show();

    }

    private void rateUs(final Activity allCategory,  final SharedPreferences.Editor editor) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(allCategory);
        LayoutInflater inflater = (LayoutInflater) allCategory.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Typeface head = Typeface.createFromAsset(allCategory.getAssets(), "lodingfont.ttf");
        View someLayout = inflater.inflate(R.layout.rateusdialog, null);
        TextView des = (TextView) someLayout.findViewById(R.id.rateusdescription);
        TextView rateuslater = (TextView) someLayout.findViewById(R.id.rateuslater);
        TextView dailogtitle = (TextView) someLayout.findViewById(R.id.rateustitle);
        TextView yestakeme = (TextView) someLayout.findViewById(R.id.yestakeme);

        dailogtitle.setTypeface(head);
        yestakeme.setTypeface(head);
        rateuslater.setTypeface(head);
        des.setTypeface(head);
        dialog.setView(someLayout);

        final AlertDialog alertDialog = dialog.create();
        yestakeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("israted",true);
                editor.commit();
                //final String appPackageName = allCategory.; // getPackageName() from Context or Activity object
                try {
                    allCategory.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=best.news.shorts.app.glance")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    allCategory.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=best.news.shorts.app.glance")));
                }
            }

        });
        rateuslater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


        alertDialog.show();

    }

    private class SendFeedBack extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                String gettoken = "";

                gettoken  = prefs.getString("token", "");

                Log.e("Token" , gettoken);
                String url = getactivity.getResources().getString(R.string.apiurl)+"/api/v1/feedback/save";
                URL object = new URL(url);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();

                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", gettoken);

                con.setRequestMethod("POST");


                String title;
                if (feedbacktext.length()>25)
                    title = feedbacktext.substring(0,25)+"...";
                else
                    title = feedbacktext;

                JSONObject cred = new JSONObject();

                cred.put("title", title);
                cred.put("message", feedbacktext);
                cred.put("stars", 0);
                cred.put("isLiked", false);

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


                    JSONObject json_data = new JSONObject(String.valueOf(sb));
                    String s = (json_data.getString("message"));
                    System.out.println(s);



                } else {
                    System.out.println("else"+con.getResponseMessage());
                }
            }catch ( Exception e){
                Log.e("Submit",e.toString());
            }
            return null;
        }
    }
}