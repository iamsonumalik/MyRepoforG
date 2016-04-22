package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.zip.Inflater;

public class Viral extends Activity {

    private static final String MY_PREFS_NAME = "MySetting";
    private SharedPreferences prefs;
    private ViewPager viewPager;
    private ArrayList<String> headlines;
    private ArrayList<String>  viraltimestampcreated;
    private ArrayList<String> _id;
    private ArrayList<String> timelinedate;
    private ArrayList<String> youtubeVideoId;
    private ArrayList<String> timelinepublicid;
    private ArrayList<String> timelinetags;
    private String tagsselction="";
    private String gettoken;
    private RelativeLayout listlayout;
    private ListView videolistview;
    private RelativeLayout loadingviewvideos;
    private ImageView fackbookimageview;
     private ImageView twitterimageview;
     private ImageView youtubeimageview;
     private ImageView vineimageview;
     private ImageView instaimageview;
    private ProgressBar progressBarforlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viral);
        initializeView();
        initializeArraylists();


        prefs = getSharedPreferences(MY_PREFS_NAME, 0);
        String subCategory = prefs.getString("videosubcategory","");
        gettoken = prefs.getString("token", "");
        String[] arraysubCategory = subCategory.split(",");


        Vector<View> vectorpages = new Vector<View>();
        //addfirstview(vectorpages);
        getCurrentView(arraysubCategory,vectorpages);
        CustomPagerAdapter adapter = new CustomPagerAdapter(getBaseContext(),vectorpages);
        viewPager.setAdapter(adapter);
        //viewPager.setPageTransformer(true, new CubeInTransformer());

    }

    private void initializeView() {
        viewPager = (ViewPager) findViewById(R.id.videopager);
        listlayout = (RelativeLayout) findViewById(R.id.listlayout);
        videolistview = (ListView) findViewById(R.id.videolistview);
        loadingviewvideos = (RelativeLayout) findViewById(R.id.loadingviewvideos);
        fackbookimageview = (ImageView)findViewById(R.id.fackbookimageview);
        twitterimageview = (ImageView) findViewById(R.id.twitterimageview);
        youtubeimageview = (ImageView) findViewById(R.id.youtubeimageview);
        vineimageview = (ImageView) findViewById(R.id.vineimageview);
        instaimageview = (ImageView) findViewById(R.id.instaimageview);
        progressBarforlist = (ProgressBar) findViewById(R.id.progressBarforlist);
        removeAllImageView(getBaseContext());
        listlayout.setVisibility(View.GONE);

    }

    private void initializeArraylists() {
        headlines = new ArrayList<String>();
        viraltimestampcreated= new ArrayList<String>();
        _id = new ArrayList<String>();
        timelinedate= new ArrayList<String>();
        youtubeVideoId= new ArrayList<String>();
        timelinepublicid= new ArrayList<String>();
        timelinetags= new ArrayList<String>();
    }

    private void addfirstview(LinearLayout pages) {
        View firstview = ((LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.viewpagerfirstview, null, false);

        LinearLayout firstpagelinearlayour = (LinearLayout) firstview.findViewById(R.id.firstpagelinearlayour);
        TextView mybookmarks = (TextView) firstview.findViewById(R.id.mybookmarks);
        mybookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavingViral savingViral = new SavingViral(Viral.this);
                try {
                    savingViral.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayList<String> temp = savingViral.getAll();

                headlines.removeAll(headlines);
                _id.removeAll(_id);
                viraltimestampcreated.removeAll(viraltimestampcreated);
                youtubeVideoId.remove(youtubeVideoId);
                timelinedate.removeAll(timelinedate);
                timelinepublicid.removeAll(timelinepublicid);
                timelinetags.removeAll(timelinetags);

                for (int i = 0; i<temp.size();i++){
                    headlines.add(temp.get(i++));
                    _id.add(temp.get(i++));
                    viraltimestampcreated.add(temp.get(i++));
                    youtubeVideoId.add(temp.get(i++));
                    timelinedate.add(temp.get(i++));
                    timelinepublicid.add(temp.get(i++));
                    timelinetags.add(temp.get(i));
                }
                Viral_ListView cv = new Viral_ListView(getBaseContext(),
                        headlines,
                        timelinedate,
                        timelinepublicid,
                        getResources(),
                        Viral.this,
                        timelinetags,
                        youtubeVideoId,
                        _id,
                        viraltimestampcreated,
                        progressBarforlist

                );
                videolistview.setAdapter(cv);
                listlayout.setVisibility(View.VISIBLE);

                try {
                    savingViral.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pages.addView(firstview);
    }

    private void getCurrentView(String[] strings,Vector<View> vectorpages) {


        int check = 0;
        int itemadd=0;
        while (itemadd<strings.length) {
            View footerView = ((LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.viewpagerlist, null, false);

            LinearLayout pages = (LinearLayout) footerView.findViewById(R.id.viewpagerlinearlayout);
            pages.setOrientation(LinearLayout.VERTICAL);
            if (check==0){
                addfirstview(pages);
            }
            for (int i = check; i < check+15; i++) {
                if (i<strings.length) {
                    final TextView textView = new TextView(Viral.this);
                    textView.setText(strings[itemadd++]);
                    textView.setTextSize(24);
                    textView.setTextColor(Color.WHITE);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tagsselction=textView.getText().toString();
                            loadingviewvideos.setVisibility(View.VISIBLE);
                            listlayout.setVisibility(View.VISIBLE);
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
                            };;
                            t.start();
                            new FetchTimeline(t).execute();


                        }
                    });
                    pages.addView(textView);
                }
            }
            vectorpages.add(pages);
            check = check+15;
        }

    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private Vector<View> pages;

        public CustomPagerAdapter(Context context, Vector<View> pages) {
            this.mContext=context;
            this.pages=pages;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = pages.get(position);
            container.addView(page);
            return page;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class FetchTimeline extends AsyncTask<String,String,String> {


        private final Thread t;
        private Viral_ListView cv;

        public FetchTimeline(Thread t) {
            this.t = t;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            headlines.removeAll(headlines);
            _id.removeAll(_id);
            viraltimestampcreated.removeAll(viraltimestampcreated);
            youtubeVideoId.remove(youtubeVideoId);
            timelinedate.removeAll(timelinedate);
            timelinepublicid.removeAll(timelinepublicid);
            timelinetags.removeAll(timelinetags);

            cv = new Viral_ListView(getBaseContext(),
                    headlines,
                    timelinedate,
                    timelinepublicid,
                    getResources(),
                    Viral.this,
                    timelinetags,
                    youtubeVideoId,
                    _id,
                    viraltimestampcreated,
                    progressBarforlist

            );
            videolistview.setAdapter(cv);

        }
        @Override
        protected String doInBackground(String... params) {

            InputStream is = null;
            String line;

            try {
                String strUrl;
                    String stags = tagsselction;
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
                    JSONObject content = data.getJSONObject("content");
                    JSONObject tags = data.getJSONObject("tags");
                    JSONObject source = data.getJSONObject("source");
                    JSONObject image = source.getJSONObject("image");
                    if(tags.getString("category").equals("VIRAL")) {
                        String h = data.getString("headline");
                        headlines.add( h);
                        _id.add(data.getString("_id"));
                        viraltimestampcreated.add(data.getString("timestampCreated"));
                        String com = content.getString("html");

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
                        for (int j = 0; j < people.length(); j++) {
                            String temptag = people.getString(j);
                            othertags += "," + temptag;
                        }
                        JSONArray place = tags.getJSONArray("place");
                        for (int j = 0; j < place.length(); j++) {
                            String temptag = place.getString(j);
                            othertags += "," + temptag;
                        }
                        timelinetags.add(othertags);
                        timelinedate.add( data.getString("youtubeVideoDuration"));
                        youtubeVideoId.add(data.getString("youtubeVideoId"));
                    }

                }
            } catch (Exception e) {

                Log.e("Viral" ,e.toString());
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

    @Override
    public void onBackPressed() {
        if (listlayout.getVisibility()==View.VISIBLE)
            listlayout.setVisibility(View.GONE);
        else
        super.onBackPressed();
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
                    fackbookimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.fw));
                    break;
                case 1:
                    twitterimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.tw));
                    break;
                case 2:
                    youtubeimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.yw));
                    break;
                case 3:
                    vineimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.vw));
                    break;
                case 4:
                    instaimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.iw));
                    break;
            }

        }
        });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeAllImageView(Context baseContext) {
        fackbookimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.fb));
        twitterimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.tb));
        youtubeimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.yb));
        vineimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.vb));
        instaimageview.setImageDrawable(baseContext.getResources().getDrawable(R.drawable.ib));
    }
}
