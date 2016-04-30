package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;


public class Viral extends Activity {

    private static final String MY_PREFS_NAME = "MySetting";
    private SharedPreferences prefs;
    private ViewPager viewPager;
    private ArrayList<String> headlines;
    private ArrayList<String> watched;
    private ArrayList<String>  viraltimestampcreated;
    private ArrayList<String> _id;
    private ArrayList<String> timelinedate;
    private ArrayList<String> youtubeVideoId;
    private ArrayList<String> timelinepublicid;
    private ArrayList<String> timelinetags;
    private ArrayList<String> timelinepeopletags;
    private ArrayList<String> timelineplacetags;
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
    private RelativeLayout filtetagslayout;
    private Button filtetagsbackbutton;
    private TextView filtetagsname;
    private ListView filtetagslistview;
    private Typeface lodingfont;
    private TextView linelodaingvideo;
    private Typeface headingfont;
    private RelativeLayout bookmarkheaderlayout;
    private Button bookmarkbackbutton;
    private TextView bookmarkname;
    private int width;
    private int height;
    private ImageView callmenuimageview;
    private TextView bookmarkfollow;
    private SharedPreferences.Editor editor;

    //00aeef
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viral);
        lodingfont = Typeface.createFromAsset(getAssets(), "lodingfont.ttf");
        headingfont = Typeface.createFromAsset(getAssets(), "headline.otf");
        initializeView();
        initializeArraylists();
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated


        prefs = getSharedPreferences(MY_PREFS_NAME, 0);
        editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        String subCategory = prefs.getString("videosubcategory","");
        gettoken = prefs.getString("token", "");
        String[] arraysubCategory = subCategory.split(",");
        Arrays.sort(arraysubCategory,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String o11 = o1.split("\\.")[0];
                String o21 = o2.split("\\.")[0];
                return Integer.valueOf(o11).compareTo(Integer.valueOf(o21));
            }
        });
        //String[] temp = new String[arraysubCategory.length/12];

        final Vector<View> vectorpages = new Vector<View>();
        //addfirstview(vectorpages);


        getCurrentView(arraysubCategory,vectorpages);
        //setPageBorder(vectorpages);
        final CustomPagerAdapter adapter = new CustomPagerAdapter(getBaseContext(),vectorpages);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(30,15,30,15);
        viewPager.setPageMargin(15);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private ViewPager   mViewPager = viewPager;
            private int         mCurrentPosition;
            private int         mScrollState;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
               /* int pageCount = adapter.getCount();
                if (position == 0){
                    viewPager.setCurrentItem(pageCount-2,false);
                } else if (position == pageCount-1){
                    viewPager.setCurrentItem(1,false);
                }*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                handleScrollState(state);
                mScrollState = state;
            }
            private void handleScrollState(final int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    setNextItemIfNeeded();
                }
            }

            private void setNextItemIfNeeded() {
                if (!isScrollStateSettling()) {
                    handleSetNextItem();
                }
            }

            private boolean isScrollStateSettling() {
                return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
            }

            private void handleSetNextItem() {
                final int lastPosition = mViewPager.getAdapter().getCount() - 1;
                if(mCurrentPosition == 0) {
                    mViewPager.setCurrentItem(lastPosition, false);
                } else if(mCurrentPosition == lastPosition) {
                    mViewPager.setCurrentItem(0, false);
                }
            }
        });
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        filtetagsbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtetagslayout.setVisibility(View.GONE);
            }
        });

        bookmarkbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // bookmarkheaderlayout.setVisibility(View.GONE);
                listlayout.setVisibility(View.GONE);
            }
        });

        callmenuimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  bookmarkheaderlayout.setVisibility(View.GONE);
                listlayout.setVisibility(View.GONE);
            }
        });

        bookmarkfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myfeed = prefs.getString("myfeed","");
                if (myfeed.contains(tagsselction)) {
                    bookmarkfollow.setText("Follow");
                    Controller.getInstance().trackEvent(tagsselction, "Follow", "user");
                    myfeed = myfeed.replaceAll(tagsselction + ",", "");
                    editor.putString("myfeed", myfeed);
                    editor.commit();
                    editor.apply();
                }
                else {
                    myfeed = myfeed + tagsselction + ",";
                    editor.putString("myfeed", myfeed);
                    editor.commit();
                    editor.apply();
                    bookmarkfollow.setText("Following");
                }
            }
        });

    }

    private void setPageBorder(Vector<View> vectorpages) {
        int size = vectorpages.size();
        for (int i = 0 ; i<size;i++)
        switch (i%5){
            case 0:
                vectorpages.get(i).setBackgroundColor(Color.RED);
                break;
            case 1:
                vectorpages.get(i).setBackgroundColor(Color.BLUE);
                break;
            case 2:
                vectorpages.get(i).setBackgroundColor(Color.BLACK);
                break;
            case 3:
                vectorpages.get(i).setBackgroundColor(Color.GREEN);
                break;
            case 4:
                vectorpages.get(i).setBackgroundColor(Color.YELLOW);
                break;
        }
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
        filtetagslayout = (RelativeLayout) findViewById(R.id.filtetagslayout);
        filtetagsbackbutton = (Button) findViewById(R.id.filtetagsbackbutton);
        filtetagsname = (TextView) findViewById(R.id.filtetagsname);
        linelodaingvideo  = (TextView) findViewById(R.id.linelodaingvideo);
        filtetagsname.setTypeface(lodingfont);
        linelodaingvideo.setTypeface(lodingfont);
        filtetagslistview = (ListView) findViewById(R.id.filtetagslistview);
        bookmarkheaderlayout = (RelativeLayout) findViewById(R.id.bookmarkheaderlayout);
        bookmarkbackbutton =  (Button) findViewById(R.id.bookmarkbackbutton);
        bookmarkname = (TextView) findViewById(R.id.bookmarkname);
        bookmarkfollow =(TextView) findViewById(R.id.bookmarkfollow);
        callmenuimageview = (ImageView) findViewById(R.id.callmenuimageview);
        removeAllImageView(getBaseContext());
        listlayout.setVisibility(View.GONE);
        //bookmarkheaderlayout.setVisibility(View.GONE);

    }

    private void initializeArraylists() {
        headlines = new ArrayList<String>();
        watched = new ArrayList<String>();
        viraltimestampcreated= new ArrayList<String>();
        _id = new ArrayList<String>();
        timelinedate= new ArrayList<String>();
        youtubeVideoId= new ArrayList<String>();
        timelinepublicid= new ArrayList<String>();
        timelinetags= new ArrayList<String>();
        timelinepeopletags= new ArrayList<String>();
        timelineplacetags= new ArrayList<String>();
    }

    private void addfirstview(LinearLayout pages) {
        View firstview = ((LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.viewpagerfirstview, null, false);

        LinearLayout firstpagelinearlayour = (LinearLayout) firstview.findViewById(R.id.firstpagelinearlayour);
        LinearLayout readnewslayout = (LinearLayout) firstview.findViewById(R.id.readnewslayout);
        LinearLayout mybookmarkslayout = (LinearLayout) firstview.findViewById(R.id.mybookmarkslayout);
        RelativeLayout myfeedlayout = (RelativeLayout) firstview.findViewById(R.id.myfeedlayout);
        TextView mybookmarks = (TextView) firstview.findViewById(R.id.mybookmarks);
        TextView myfeedtv = (TextView) firstview.findViewById(R.id.myfeed);
        ImageView filtetagsiconblack = (ImageView) firstview.findViewById(R.id.filtetagsiconblack);
        TextView videolisttitle = (TextView) firstview.findViewById(R.id.videolisttitle);
        TextView readnews = (TextView) firstview.findViewById(R.id.readnews);

        mybookmarkslayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-30)/11 , 1f));
        myfeedlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,(height-30)/11 , 1f));
        videolisttitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-30)/11 , 1f));
        readnewslayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-30)/11 , 1f));

        mybookmarks.setGravity(Gravity.CENTER_VERTICAL);
        videolisttitle.setGravity(Gravity.CENTER_VERTICAL);
        mybookmarks.setTypeface(lodingfont);
        myfeedtv.setTypeface(lodingfont);
        bookmarkname.setTypeface(lodingfont);
        bookmarkfollow.setTypeface(lodingfont);
        videolisttitle.setTypeface(headingfont);
        readnews.setTypeface(lodingfont);
        myfeedtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Controller.getInstance().trackEvent("My Feed", "Videos", "user");
                bookmarkfollow.setVisibility(View.GONE);
                String myfeed = prefs.getString("myfeed","");
                if (!myfeed.equals("")) {
                    bookmarkheaderlayout.setVisibility(View.VISIBLE);
                    bookmarkname.setText("My Feed");
                    tagsselction ="~Recommended,"+ myfeed.substring(0, myfeed.length() - 1);
                    generateList();
                }else {

                    bookmarkheaderlayout.setVisibility(View.VISIBLE);
                    bookmarkname.setText("My Feed");
                    tagsselction ="~Recommended";
                    generateList();
                }
            }
        });


        readnewslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LoadingActivity.class);
                intent.putExtra("fromnoti", false);
                intent.putExtra("news",true);
                startActivity(intent);
                finish();
            }
        });
        filtetagsiconblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myfeed = prefs.getString("myfeed","");
                if (!myfeed.equals("")) {

                    filtetagslayout.setVisibility(View.VISIBLE);
                    String[] myfeedarray = myfeed.substring(0, myfeed.length() - 1).split(",");
                    Custom_FilterVIew custom_filterVIew = new Custom_FilterVIew(getBaseContext(),
                            myfeedarray, getResources(), Viral.this,
                            prefs);
                    filtetagslistview.setAdapter(custom_filterVIew);
                }else {
                }
            }
        });

        mybookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.getInstance().trackEvent("My Bookmarks", "Videos", "user");
                bookmarkfollow.setVisibility(View.GONE);
                bookmarkheaderlayout.setVisibility(View.VISIBLE);
                bookmarkname.setText("My Bookmarks");
                SavingViral savingViral = new SavingViral(Viral.this);
                try {
                    savingViral.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayList<String> temp = savingViral.getAll();

                headlines.removeAll(headlines);
                watched.removeAll(watched);
                _id.removeAll(_id);
                viraltimestampcreated.removeAll(viraltimestampcreated);
                youtubeVideoId.removeAll(youtubeVideoId);
                timelinedate.removeAll(timelinedate);
                timelinepublicid.removeAll(timelinepublicid);
                timelinetags.removeAll(timelinetags);
                timelinepeopletags.removeAll(timelinepeopletags);
                timelineplacetags.removeAll(timelineplacetags);

                for (int i = 0; i<temp.size();i++){
                    headlines.add(temp.get(i++));
                    _id.add(temp.get(i++));
                    viraltimestampcreated.add(temp.get(i++));
                    youtubeVideoId.add(temp.get(i++));
                    timelinedate.add(temp.get(i++));
                    timelinepublicid.add(temp.get(i++));
                    timelinetags.add(temp.get(i++));
                    timelinepeopletags.add(temp.get(i++));
                    timelineplacetags.add(temp.get(i));
                    watched.add("");

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
                        progressBarforlist,
                        timelinepeopletags,
                        timelineplacetags,
                        watched

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
                for (int i = check; i < check+7; i++) {
                    if (i<strings.length) {
                        final TextView textView = new TextView(Viral.this);
                        textView.setText(strings[itemadd++]);
                        textView.setTypeface(lodingfont);
                        textView.setTextSize(24);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setTextColor(Color.WHITE);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-30)/11 , 1f));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String temp = textView.getText().toString();
                                Controller.getInstance().trackEvent(temp, "Sub Category", "user");
                                tagsselction = temp;
                                bookmarkname.setText(tagsselction);
                                bookmarkheaderlayout.setVisibility(View.VISIBLE);
                                bookmarkfollow.setVisibility(View.VISIBLE);
                                String myfeed = prefs.getString("myfeed","");
                                if (myfeed.contains(tagsselction))
                                    bookmarkfollow.setText("Followig");
                                else
                                    bookmarkfollow.setText("Follow");



                                generateList();
                            }
                        });
                        pages.addView(textView);
                    }
                }
                vectorpages.add(pages);
                check = check+7;
            }else {
                for (int i = check; i < check + 10; i++) {
                    if (i < strings.length) {
                        final TextView textView = new TextView(Viral.this);
                        textView.setText(strings[itemadd++]);
                        textView.setTypeface(lodingfont);
                        textView.setTextSize(24);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setTextColor(Color.WHITE);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height-30)/10 , 1f));
                        //textView.setPadding(0, 10, 10, 10);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String temp = textView.getText().toString();
                                tagsselction = temp;
                                bookmarkname.setText(tagsselction);
                                bookmarkheaderlayout.setVisibility(View.VISIBLE);
                                bookmarkfollow.setVisibility(View.VISIBLE);
                                String myfeed = prefs.getString("myfeed","");
                                if (myfeed.contains(tagsselction))
                                    bookmarkfollow.setText("Followig");
                                else
                                    bookmarkfollow.setText("Follow");
                                generateList();
                            }
                        });
                        pages.addView(textView);
                    }
                }
                vectorpages.add(pages);
                check = check + 10;
            }
        }

    }

    private void generateList() {
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

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private Vector<View> pages;

        public CustomPagerAdapter(Context context, Vector<View> pages) {
            this.mContext=context;
            this.pages=pages;
            /*
            int actualNoOfIDs = pages.size();
            count = actualNoOfIDs + 2;
            Vector<View> temppages = new Vector<View>();
           // pageIDsArray[0] = pageIDs[actualNoOfIDs - 1];
            temppages.add(0,new View(Viral.this));
            for (int i = 0; i < actualNoOfIDs; i++) {
               // pageIDsArray[i + 1] = pageIDs[i];
                temppages.add(i+1,pages.get(i));
            }
            temppages.add(count - 1,new View(Viral.this));
           // pageIDsArray[count - 1] = pageIDs[0];

            //pages.removeAll(pages);
            this.pages = temppages;   */
        }


        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = pages.get(position);
            container.addView(page);
            return page;
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
        ArrayList<String> addatend;
        public FetchTimeline(Thread t) {
            this.t = t;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            addatend = new ArrayList<String>();
            headlines.removeAll(headlines);
            watched.removeAll(watched);
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
                    Viral.this,
                    timelinetags,
                    youtubeVideoId,
                    _id,
                    viraltimestampcreated,
                    progressBarforlist,
                    timelinepeopletags,
                    timelineplacetags,
                    watched

            );
            videolistview.setAdapter(cv);

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

                SavingWatched savingWatched = new SavingWatched(Viral.this);
                savingWatched.open();
                for (int i=0;i<count;i++){
                    JSONObject data = array.getJSONObject(i);
                    JSONObject publish = data.getJSONObject("publish");
                    JSONObject portrait = publish.getJSONObject("portrait");
                    JSONObject url_id = portrait.getJSONObject("url");
                    JSONObject tags = data.getJSONObject("tags");

                    if(tags.getString("category").equals("VIRAL")) {
                        if (!savingWatched.getData(data.getString("_id"))) {
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
                            watched.add("");
                            Log.e("youtubeVideoId", data.getString("youtubeVideoId"));
                        }else {
                            addatend.add(data.getString("_id"));

                        }
                    }



                }
                for (int x =0 ; x<addatend.size();x++ ){
                    watched.add("Watched");
                    ArrayList<String> temp = savingWatched.getAll(addatend.get(x));

                    for (int i = 0; i<temp.size();i++){
                        headlines.add(temp.get(i++));
                        _id.add(temp.get(i++));
                        viraltimestampcreated.add(temp.get(i++));
                        youtubeVideoId.add(temp.get(i++));
                        timelinedate.add(temp.get(i++));
                        timelinepublicid.add(temp.get(i++));
                        timelinetags.add(temp.get(i++));
                        timelinepeopletags.add(temp.get(i++));
                        timelineplacetags.add(temp.get(i));

                    }
                }
                savingWatched.close();
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
        //bookmarkheaderlayout.setVisibility(View.GONE);
        if (filtetagslayout.getVisibility() == View.VISIBLE)
            filtetagslayout.setVisibility(View.GONE);
        else if (listlayout.getVisibility()==View.VISIBLE)
            listlayout.setVisibility(View.GONE);
        else{
            startActivity(new Intent(Viral.this,MainActivity.class));
            finish();
        }
        //super.onBackPressed();
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
                Thread.sleep(300);
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
