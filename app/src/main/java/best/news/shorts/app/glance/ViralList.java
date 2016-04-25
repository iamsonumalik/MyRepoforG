package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViralList extends Activity {

    private static final String MY_PREFS_NAME = "MySetting";
    private String tagsselction;
    private ArrayList<String> headlines;
    private ArrayList<String>  viraltimestampcreated;
    private ArrayList<String> _id;
    private ArrayList<String> timelinedate;
    private ArrayList<String> youtubeVideoId;
    private ArrayList<String> timelinepublicid;
    private ArrayList<String> timelinetags;

    private ArrayList<String> timelinepeopletags;
    private ArrayList<String> timelineplacetags;
    private ListView viraltagslistview;
    private Object gettoken;
    private SharedPreferences prefs;
    private RelativeLayout headerlayout;
    private Button backtagsbutton;
    private TextView tagname;
    private TextView followtag;
    private String myfeed;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBarforlist;
    private Typeface lodingfont;
    private RelativeLayout loadingviewvideos;
    private ImageView twitterimageview;
    private ImageView fackbookimageview;
    private ImageView youtubeimageview;
    private ImageView vineimageview;
    private TextView linelodaingvideo;
    private ImageView instaimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viral_list);
        initializeArraylists();
        initializeView();
        lodingfont = Typeface.createFromAsset(getAssets(), "lodingfont.ttf");

        tagsselction = getIntent().getStringExtra("tag");
        prefs = getSharedPreferences(MY_PREFS_NAME, 0);
        editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        gettoken = prefs.getString("token", "");
        myfeed = prefs.getString("myfeed", "");

        if (myfeed.contains(tagsselction)){
            followtag.setText("Following");

        }else {
            followtag.setText("Follow");
        }

        tagname.setTypeface(lodingfont);
        followtag.setTypeface(lodingfont);

        tagname.setText(tagsselction);
        loadingviewvideos.setVisibility(View.VISIBLE);
        Thread t = new Thread(){
            boolean w = true;
            @Override
            public void interrupt() {
                w = false;
                //super.interrupt();
            }

            @Override
            public void run() {
                super.run();
                while (w){
                    doit(getBaseContext());
                }

            }
        };t.start();
        new FetchTimeline(t).execute();

        backtagsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        followtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myfeed.contains(tagsselction)){
                    myfeed = myfeed.replaceAll(tagsselction+",","");
                    editor.putString("myfeed", myfeed);
                    editor.commit();
                    editor.apply();
                    followtag.setText("Follow");
                }else {
                    myfeed = myfeed + tagsselction + ",";
                    editor.putString("myfeed", myfeed);
                    editor.commit();
                    editor.apply();
                    followtag.setText("Following");
                }
            }
        });
    }

    private void initializeView() {
        viraltagslistview = (ListView) findViewById(R.id.viraltagslistview);
        headerlayout =(RelativeLayout) findViewById(R.id.headerlayout);
        backtagsbutton = (Button) findViewById(R.id.backtagsbutton);
        tagname = (TextView) findViewById(R.id.tagname);
        followtag =(TextView) findViewById(R.id.followtag);
        progressBarforlist = (ProgressBar) findViewById(R.id.progressBarforlist);
        loadingviewvideos = (RelativeLayout) findViewById(R.id.loadingviewvideos);
        fackbookimageview = (ImageView)findViewById(R.id.fackbookimageview);
        twitterimageview = (ImageView) findViewById(R.id.twitterimageview);
        youtubeimageview = (ImageView) findViewById(R.id.youtubeimageview);
        vineimageview = (ImageView) findViewById(R.id.vineimageview);
        instaimageview = (ImageView) findViewById(R.id.instaimageview);
        linelodaingvideo  = (TextView) findViewById(R.id.linelodaingvideo);
        linelodaingvideo.setTypeface(lodingfont);


        removeAllImageView(getBaseContext());
    }

    private void removeAllImageView(Context baseContext) {
        fackbookimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.fw));
        twitterimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.tw));
        youtubeimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.yw));
        vineimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.vw));
        instaimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.iw));
    }

    private void initializeArraylists() {
        headlines = new ArrayList<String>();
        viraltimestampcreated= new ArrayList<String>();
        _id = new ArrayList<String>();
        timelinedate= new ArrayList<String>();
        youtubeVideoId= new ArrayList<String>();
        timelinepublicid= new ArrayList<String>();
        timelinetags= new ArrayList<String>();

        timelinepeopletags= new ArrayList<String>();
        timelineplacetags= new ArrayList<String>();
    }
    private class FetchTimeline extends AsyncTask<String,String,String> {


        private final Thread t;
        private Viral_ListView cv;

        public FetchTimeline(Thread t) {
            this.t= t;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            headlines.removeAll(headlines);
            _id.removeAll(_id);
            viraltimestampcreated.removeAll(viraltimestampcreated);
            youtubeVideoId.removeAll(youtubeVideoId);
            timelinedate.removeAll(timelinedate);
            timelinepublicid.removeAll(timelinepublicid);
            timelinetags.removeAll(timelinetags);
            timelinepeopletags.removeAll(timelinepeopletags);
            timelineplacetags.removeAll(timelineplacetags);

            cv = new Viral_ListView(getBaseContext(),
                    headlines,
                    timelinedate,
                    timelinepublicid,
                    getResources(),
                    ViralList.this,
                    timelinetags,
                    youtubeVideoId,
                    _id,
                    viraltimestampcreated,
                    progressBarforlist,
                    timelinepeopletags,
                    timelineplacetags

            );
            viraltagslistview.setAdapter(cv);

        }
        @Override
        protected String doInBackground(String... params) {

            InputStream is = null;
            String line;

            try {
                String strUrl;
                String stags = tagsselction.replaceAll("#","%23");
                String fstags = (new StringBuilder()).append("%5B%22")
                        .append(stags.replaceAll(",", "%22%2C%22"))
                        .append("%22%5D").toString();
                strUrl = (new StringBuilder()).append(getResources().getString(R.string.apiurl))
                        .append("/api/v1/news/relatedBytag/")
                        .append(fstags).append("?apiKey=")
                        .append(gettoken).toString();
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
                String result = sb.toString();
                Log.e("pass 2", "connection success ");
                JSONObject json_data = new JSONObject(result);
                JSONArray array = json_data.getJSONArray("data");
                int count = json_data.getInt("count");

                for (int i=0;i<count;i++){
                    JSONObject data = array.getJSONObject(i);
                    JSONObject publish = data.getJSONObject("publish");
                    JSONObject portrait = publish.getJSONObject("portrait");
                    JSONObject url_id = portrait.getJSONObject("url");
                    JSONObject tags = data.getJSONObject("tags");

                    if(tags.getString("category").equals("VIRAL")) {
                        String h = data.getString("headline");
                        headlines.add(h);
                        _id.add(data.getString("_id"));
                        viraltimestampcreated.add(data.getString("timestampCreated"));

                        String p_id = "";
                        try {
                            p_id = url_id.getString("public_id");

                        } catch (Exception e) {
                            p_id = url_id.getString("filename");

                        }
                        timelinepublicid.add(p_id);

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
                        String peopletags = "";
                        for (int j = 0; j < people.length(); j++) {
                            String temptag = people.getString(j);
                            if (people.length() == 1) {
                                peopletags += temptag;
                            } else if (j == 0) {
                                peopletags += temptag;
                            } else if (j == (people.length() - 1)) {
                                peopletags += "," + temptag;
                            } else {
                                peopletags += "," + temptag;

                            }
                        }
                        JSONArray place = tags.getJSONArray("place");
                        String placetags = "";
                        for (int j = 0; j < place.length(); j++) {
                            String temptag = place.getString(j);
                            if (place.length() == 1) {
                                placetags += temptag;
                            } else if (j == 0) {
                                placetags += temptag;
                            } else if (j == (place.length() - 1)) {
                                placetags += "," + temptag;
                            } else {
                                placetags += "," + temptag;
                            }
                        }

                        timelineplacetags.add(placetags);
                        timelinepeopletags.add(peopletags);
                        timelinetags.add(othertags);
                        timelinedate.add(data.getString("youtubeVideoDuration"));
                        youtubeVideoId.add(data.getString("youtubeVideoId"));
                    }

                }
            } catch (Exception e) {

                Log.e("ViralList" ,e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            cv.notifyDataSetChanged();
            t.interrupt();
            loadingviewvideos.setVisibility(View.GONE);

        }
    }

    private void doit(final Context baseContext) {
        for (int i = 0; i < 5 ; i++) {

            final int finalI = i;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeAllImageView(baseContext);
                    switch (finalI){
                        case 0:
                            fackbookimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.fb));
                            break;
                        case 1:
                            twitterimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.tb));
                            break;
                        case 2:
                            youtubeimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.yb));
                            break;
                        case 3:
                            vineimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.vb));
                            break;
                        case 4:
                            instaimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.ib));
                            break;
                    }

                }
            });

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
