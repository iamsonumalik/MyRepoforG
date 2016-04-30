package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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


public class AllCategory extends Activity implements android.view.View.OnClickListener
{

    private static final String MY_PREFS_NAME = "MySetting";
    private ArrayList<String> _id;
    private FullScreenImageAdapter adapter;
    private LinearLayout all;
    private TextView allviewshare;
    private ImageView backbookmark;
    private RelativeLayout bookmarklayout;
    private LinearLayout business;
    private RelativeLayout buttonlayout;
    private RelativeLayout close2;
    private ArrayList<String> contents;
    private int currenPosition=0;
    private String currentCategory;
    boolean doubleBackToExitPressedOnce=false;
    private android.content.SharedPreferences.Editor editor;
    private LinearLayout entertainment;
    private Typeface face;
    private ProgressBar firstBar;
    private String getting_id;
    private String gettoken;
    private Button goupbutton;
    private ArrayList<String> headlines;
    private ArrayList<String> youtubeVideoId;
    private int height;
    private TextView imagecredit;
    private LinearLayout imagecreditlayout;
    private LinearLayout india;
    private LinearLayout infolayout;
    private ArrayList<String> isS3lis;
    private boolean isnews;
    private boolean isviral;
    private ArrayList<String> linktonews;
    private ListView listview;
    private TextView loadingtimelinetv;
    private Button moreonbutton;
    private String  moveto = "";
    private String mycollection = "mycollection";
    private String name;
    private String notimeline = "No Timeline for this News.";
    private boolean onceTime = true;
    private ArrayList<String> othertags;
    private VerticalPager pager;
    private SharedPreferences prefs;
    private ArrayList<String> public_idlist;
    RelativeLayout refresh;
    private String result;
    private LinearLayout science;
    private TextView seebookmark;
    private ImageView setbookmark;
    RelativeLayout share;
    private LinearLayout simplified;
    private LinearLayout sports;
    private ListView taglist;
    private ArrayList<String> tagsji;
    private RelativeLayout tagslayout;
    private String tagsselction = "";
    private ImageView tagsselectionimageview;
    private ArrayList<String> temp;
    private String temptags[];
    private ArrayList<String> timelinecredits;
    private ArrayList<String> timelinedate;
    private String  timelineon = "Timeline on:";
    private ArrayList<String> timelinepublicid;
    private ArrayList<Boolean> timelines3;
    private ArrayList<String> timestamplist;
    private boolean videopost;
    private int videopostCounter;
    private ViewPager viewPager;
    private LinearLayout viral;
    LinearLayout watch;
    private TextView watchvideotv;
    private int width;
    private LinearLayout world;
    private TextView youareoffline;
    private String youtubelink;

    public AllCategory()
    {
        doubleBackToExitPressedOnce = false;
        moveto = "";
        currenPosition = 0;
        onceTime = true;
        mycollection = "mycollection";
        timelineon = "Timeline on:";
        notimeline = "No Timeline for this News.";
        tagsselction = "";
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        Intent i = getIntent();
        moveto = i.getStringExtra("moveto");
        isnews = i.getBooleanExtra("isnews", true);
        boolean fromnoti = i.getBooleanExtra("fromnoti", false);

        isviral = false;
        currentCategory = "ALL";
        Controller.getInstance().trackScreenView("All");
        //Intialize ArrayLists
        initializeArrayLists();

        //Intialize Views
        initializeviews();
        pager = (VerticalPager) findViewById(R.id.verticalpager);

        //SharedPreferences
        editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        buttonlayout.setVisibility(View.VISIBLE);
        editor.putBoolean("isfirst", true);
        editor.commit();
        editor.apply();


        prefs = getSharedPreferences(MY_PREFS_NAME, 0);
        final boolean isfirst = prefs.getBoolean("isfirst", false);
        final boolean israted = prefs.getBoolean("israted",false);
        final boolean didyes = prefs.getBoolean("didyes",false);
        final int[] count = {prefs.getInt("count", 0)};
        videopost = prefs.getBoolean("videopost", false);
        videopostCounter = prefs.getInt("videopostCounter",0);

        buttonclicks();

        listview.setVisibility(View.GONE);
        if (!(CheckNetworkConnection.isConnectionAvailable(getBaseContext()))){
            showCustomAlert();
        }

        gettoken = prefs.getString("token", "");

        Typeface cont = Typeface.createFromAsset(getAssets(), "headline.otf");
        face = Typeface.createFromAsset(getAssets(), "lodingfont.ttf");
        loadingtimelinetv.setTypeface(cont);
        youareoffline.setTypeface(face);
        allviewshare.setTypeface(face);
        seebookmark.setTypeface(face);
        watchvideotv.setTypeface(face);
        imagecredit.setTypeface(cont);


        new CheckUpdate(gettoken, this).execute();

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        viewPager.setLayoutParams(layoutParams);
        setSwitchContent(isnews);


        viewPager.addOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int j)
            {
            }

            public void onPageScrolled(int j, float f, int k)
            {
            }

            public void onPageSelected(int position)
            {
                currenPosition = position;
                bookmarkswitcher();
                onceTime = true;
                tagsselction = "";
                loadingtimelinetv.setVisibility(View.GONE);
                videopost = prefs.getBoolean("videopost", false);
                videopostCounter = prefs.getInt("videopostCounter", 0);
                android.widget.RelativeLayout.LayoutParams layoutparams = new android.widget.RelativeLayout.LayoutParams(width, 0);
                listview.setLayoutParams(layoutparams);


                if (count[0] > 100 && !israted) {
                    new MakeRatingDialog(AllCategory.this, editor, didyes);
                    count[0] = 0;
                    editor.putInt("count", count[0]);
                    editor.commit();
                    editor.apply();
                } else if (!israted) {
                    count[0] = count[0] + 1;
                    editor.putInt("count", count[0]);
                }


                Controller.getInstance().trackScreenView(getBaseContext().getResources().getString(R.string.url) + name + ".png");
                name = public_idlist.get(position);
                if ((!(CheckNetworkConnection.isConnectionAvailable(getBaseContext())) && position%3==0)) {
                    showCustomAlert();
                }
                removeOptions();
                listview.setVisibility(View.GONE);
                getLink();
                setVisiblityofwatchbutton();
                if (position > public_idlist.size() - 7) {
                    new ExtendCategory().execute();
                }
            }
        });

        final boolean[] iup = {true};
        final boolean[] idown = {true};

        pager.addOnScrollListener(new VerticalPager.OnScrollListener() {

            public void onScroll(int scrollX)
            {
                if (scrollX < -100) {
                    iup[0] = false;
                    removeOptions();
                    callMenu();
                    countit.cancel();
                } else if (scrollX < 0 && scrollX > -100) {
                    //idown[0] = false;
                }
            }

            public void onViewScrollFinished(int currentPage)
            {
                moveto = "";
                if (currentPage == 1) {
                    if (!(CheckNetworkConnection.isConnectionAvailable(getBaseContext()))) {
                        showCustomAlert();
                    }
                    if (onceTime)
                    {
                        onceTime = false;
                        new FetchTimeline(true).execute();
                        setLoadingON(othertags.get(currenPosition));
                    }
                    countit.cancel();
                    moreonbutton.setVisibility(View.VISIBLE);
                    removeOptions();
                } else
                {
                    moreonbutton.setVisibility(View.GONE);
                }
            }

        });
        bookmarkswitcher();
        if (fromnoti)
        {
            Controller.getInstance().trackEvent("BreakingNews", "Clicked", "user");
        } else {
           // callMenu();
        }
        //close2.setVisibility(View.GONE);
    }

    private void bookmarkswitcher()
    {

        try {
            SavingBookmark savingbookmark = new SavingBookmark(this);
            savingbookmark.open();


            if (!savingbookmark.checkID(_id.get(currenPosition))) {
                setbookmark.setImageDrawable(getResources().getDrawable(R.drawable.b_no));
            } else {
                setbookmark.setImageDrawable(getResources().getDrawable(R.drawable.b_yes));
            }
            savingbookmark.close();
        } catch (Exception e) {
        e.printStackTrace();
        }



    }

    private void buttonclicks()
    {
        share.setOnClickListener(this);
        refresh.setOnClickListener(this);
        watch.setOnClickListener(this);
        goupbutton.setOnClickListener(this);
        infolayout.setOnClickListener(this);
        moreonbutton.setOnClickListener(this);
        tagslayout.setOnClickListener(this);
        setbookmark.setOnClickListener(this);
        seebookmark.setOnClickListener(this);
        backbookmark.setOnClickListener(this);
        tagsselectionimageview.setOnClickListener(this);
    }

    private void callMenu()
    {

        final GridLayout close = (GridLayout) findViewById(R.id.backlay);
        close2.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width/4,width/4);

        ImageView topiv = (ImageView) findViewById(R.id.topiv);
        topiv.setLayoutParams(layoutParams);
        all.setPadding(width / 24, width / 24, width / 24, width / 24);


        ImageView explainersiv = (ImageView) findViewById(R.id.explainersiv);
        explainersiv.setLayoutParams(layoutParams);
        simplified.setPadding(width / 24, width / 24, width / 24, width / 24);

        india = (LinearLayout) findViewById(R.id.indiamenu);
        ImageView indiaiv = (ImageView) findViewById(R.id.indiaiv);
        indiaiv.setLayoutParams(layoutParams);
        india.setPadding(width / 24, width / 24, width / 24, width / 24);

        world = (LinearLayout) findViewById(R.id.worldmenu);
        ImageView worldiv = (ImageView) findViewById(R.id.worldiv);
        worldiv.setLayoutParams(layoutParams);
        world.setPadding(width / 24, width / 24, width / 24, width / 24);

        entertainment = (LinearLayout) findViewById(R.id.entertainmentmenu);
        ImageView entertainmentiv = (ImageView) findViewById(R.id.entertainmentiv);
        entertainmentiv.setLayoutParams(layoutParams);
        entertainment.setPadding(width / 24, width / 24, width / 24, width / 24);

        sports = (LinearLayout) findViewById(R.id.sportsmenu);
        ImageView sportsiv = (ImageView) findViewById(R.id.sportsiv);
        sportsiv.setLayoutParams(layoutParams);
        sports.setPadding(width / 24, width / 24, width / 24, width / 24);

        science = (LinearLayout) findViewById(R.id.sciencemenu);
        ImageView scienceiv = (ImageView) findViewById(R.id.scienceiv);
        scienceiv.setLayoutParams(layoutParams);
        science.setPadding(width / 24, width / 24, width / 24, width / 24);

        business = (LinearLayout) findViewById(R.id.businessmenu);
        ImageView businessiv = (ImageView) findViewById(R.id.businessiv);
        businessiv.setLayoutParams(layoutParams);
        business.setPadding(width / 24, width / 24, width / 24, width / 24);

        viral = (LinearLayout) findViewById(R.id.viralmenu);
        ImageView viraliv = (ImageView) findViewById(R.id.viraliv);
        viraliv.setLayoutParams(layoutParams);
        viral.setPadding(width / 24, width / 24, width / 24, width / 24);
        TextView indiatv = (TextView) findViewById(R.id.indiatv);
        indiatv.setTypeface(face);
        TextView worldtv = (TextView) findViewById(R.id.worldtv);
        worldtv.setTypeface(face);
        TextView sciencetv = (TextView) findViewById(R.id.sciencetv);
        sciencetv.setTypeface(face);
        TextView sportstv = (TextView) findViewById(R.id.sportstv);
        sportstv.setTypeface(face);
        TextView businesstv = (TextView) findViewById(R.id.businesstv);
        businesstv.setTypeface(face);
        TextView explainertv = (TextView) findViewById(R.id.explainertv);
        explainertv.setTypeface(face);
        TextView entertainmenttv = (TextView) findViewById(R.id.entertainmenttv);
        entertainmenttv.setTypeface(face);
        TextView viraltv = (TextView) findViewById(R.id.viraltv);
        viraltv.setTypeface(face);
        TextView toptv = (TextView) findViewById(R.id.toptv);
        toptv.setTypeface(face);
        Animation anim2 = AnimationUtils.loadAnimation(
                AllCategory.this,R.anim.bounce
        );
        anim2.setDuration(1000);
        viral.setAnimation(anim2);
        all.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removeBorder();
                //all.setBackground(getResources().getDrawable(R.drawable.menuborder));
                Controller.getInstance().trackEvent("ALL", "Menu", "user");
                Controller.getInstance().trackScreenView("All");
                isviral = false;
                currentCategory = "ALL";
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getAll();
                    savingPublicId.close();
                }catch (Exception e){

                }
                resetViewPager();

                close2.setVisibility(View.GONE);
            }

        });
        science.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removeBorder();
                science.setBackground(getResources().getDrawable(R.drawable.menuborder));
                Controller.getInstance().trackEvent("Science", "Menu", "user");
                Controller.getInstance().trackScreenView("Science");
                isviral = false;
                currentCategory = "SCIENCE_TECH";
                currenPosition = 0;moveto="";
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getByCategory("SCIENCE_TECH");
                    savingPublicId.close();
                }catch (Exception e){
                    Log.e("Science", e.toString());
                }
                resetViewPager();
                close2.setVisibility(View.GONE);
            }

        });
        sports.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBorder();
                sports.setBackground(getResources().getDrawable(R.drawable.menuborder));
                Controller.getInstance().trackEvent("Sports", "Menu", "user");
                Controller.getInstance().trackScreenView("Sports");
                isviral = false;
                currentCategory = "SPORTS";
                currenPosition = 0;
                moveto = "";
                try {
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getByCategory("SPORTS");
                    savingPublicId.close();
                } catch (Exception e) {
                    Log.e("Science", e.toString());
                }
                resetViewPager();
                close2.setVisibility(View.GONE);
            }

        });
        entertainment.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removeBorder();
                entertainment.setBackground(getResources().getDrawable(R.drawable.menuborder));
                Controller.getInstance().trackEvent("Entertainment", "Menu", "user");
                Controller.getInstance().trackScreenView("Entertainment");
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getByCategory("ENTERTAINMENT");
                    savingPublicId.close();
                }catch (Exception e){
                    Log.e("Science", e.toString());
                }
                isviral = false;moveto="";
                currentCategory = "ENTERTAINMENT";
                currenPosition = 0;
                resetViewPager();
                close2.setVisibility(View.GONE);
            }

        });
        world.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removeBorder();
                world.setBackground(getResources().getDrawable(R.drawable.menuborder));


                Controller.getInstance().trackEvent("World", "Menu", "user");
                Controller.getInstance().trackScreenView("World");
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getByCategory("WORLD");
                    savingPublicId.close();
                }catch (Exception e){
                    Log.e("Science", e.toString());
                }
                isviral = false;
                currentCategory = "WORLD";
                currenPosition = 0;moveto="";
                resetViewPager();
                close2.setVisibility(View.GONE);
            }

        });
        india.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removeBorder();
                india.setBackground(getResources().getDrawable(R.drawable.menuborder));
                Controller.getInstance().trackEvent("India", "Menu", "user");
                Controller.getInstance().trackScreenView("India");
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getByCategory("INDIA");
                    savingPublicId.close();
                }catch (Exception e){
                    Log.e("Science", e.toString());
                }
                isviral = false;
                currentCategory = "INDIA";
                currenPosition = 0;moveto="";
                resetViewPager();
                close2.setVisibility(View.GONE);
            }

        });
        simplified.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removeBorder();
                simplified.setBackground(getResources().getDrawable(R.drawable.menuborder));
                Controller.getInstance().trackEvent("Simplified", "Menu", "user");
                Controller.getInstance().trackScreenView("Simplified");
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getSimplified("true");
                    savingPublicId.close();
                }catch (Exception e){
                    Log.e("Businness", e.toString());
                }
                isviral = false;
                currentCategory = "SIMPLIFIED";
                currenPosition = 0;moveto="";
                resetViewPager();
                close2.setVisibility(View.GONE);
            }

        });
        business.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBorder();
                business.setBackground(getResources().getDrawable(R.drawable.menuborder));

                Controller.getInstance().trackEvent("Business", "Menu", "user");
                Controller.getInstance().trackScreenView("Business");
                try{
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    temp = savingPublicId.getByCategory("BUSINESS");
                    savingPublicId.close();
                }catch (Exception e){
                    Log.e("Science", e.toString());
                }
                isviral = false;
                currentCategory = "BUSINESS";
                currenPosition = 0;moveto="";
                resetViewPager();
                close2.setVisibility(View.GONE);

            }
        });
        viral.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if (CheckNetworkConnection.isConnectionAvailable(getBaseContext())) {
                    isviral = true;
                    backbookmark.setVisibility(View.GONE);
                    Intent intent = new Intent(getBaseContext(), LoadingActivity.class);
                    intent.putExtra("fromnoti", false);
                    intent.putExtra("news", false);
                    startActivity(intent);
                    finish();
                }else {
                    youareoffline.setVisibility(View.VISIBLE);
                    youareofflineC.cancel();
                    youareofflineC.start();
                }
            }

        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close2.setVisibility(View.GONE);
            }
        });
        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close2.setVisibility(View.GONE);
            }
        });
    }

    private void closeApp()
    {
        if (doubleBackToExitPressedOnce)
        {
            //super.onBackPressed();
            startActivity(new Intent(AllCategory.this,MainActivity.class));
            finish();
        } else
        {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            (new Handler()).postDelayed(new Runnable() {

                public void run()
                {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000L);
        }
    }

    private void fromdatabase(ArrayList arraylist)
    {
        if (currenPosition == 0)
        {
            public_idlist.removeAll(public_idlist);
            othertags.removeAll(othertags);
            linktonews.removeAll(linktonews);
            _id.removeAll(_id);
            timestamplist.removeAll(timestamplist);
            isS3lis.removeAll(isS3lis);
        }
        int index=0;
        ArrayList<String> tempPid = new ArrayList<String>();
        ArrayList<String> tempothertags = new ArrayList<String>();
        ArrayList<String> temp_id = new ArrayList<String>();
        ArrayList<String> templinktonews = new ArrayList<String>();
        ArrayList<String> temptimestamplist = new ArrayList<String>();
        ArrayList<String> tempisS3lis = new ArrayList<String>();
        for (int t = 0;t<temp.size();t++){
            tempPid.add(index,temp.get(t++));
            tempothertags.add(index,temp.get(t++));
            temp_id.add(index,temp.get(t++));
            templinktonews.add(index,temp.get(t++));
            temptimestamplist.add(index,temp.get(t++));
            tempisS3lis.add(index++,temp.get(t));
        }

        temp.removeAll(temp);
        int t=0;
        while (tempPid.size()>0){
            if(templinktonews.get(t).equals("n")){
                setArraylists(t,tempPid,tempothertags,temp_id,
                        templinktonews,temptimestamplist
                        ,tempisS3lis);
            }else {
                searchLink(tempPid, tempothertags, temp_id, templinktonews.get(t),
                        templinktonews,temptimestamplist,
                        tempisS3lis);
                setArraylists(t, tempPid, tempothertags, temp_id,
                        templinktonews,temptimestamplist,
                         tempisS3lis);
            }
            //t++;
        }
    }

    private void initializeArrayLists()
    {
        temp = new ArrayList<String>();
        othertags = new ArrayList<String>();
        public_idlist = new ArrayList<String>();
        _id = new ArrayList<String>();
        linktonews = new ArrayList<String>();
        contents = new ArrayList<String>();
        headlines = new ArrayList<String>();
        youtubeVideoId = new ArrayList<String>();
        timelinedate = new ArrayList<String>();
        timelinepublicid = new ArrayList<String>();
        timelinecredits = new ArrayList<String>();
        timelines3 = new ArrayList<Boolean>();
        timestamplist = new ArrayList<String>();
        isS3lis= new ArrayList<String>();    }

    private void initializeviews()
    {
        close2 = (RelativeLayout) findViewById(R.id.backlay2);
        taglist = (ListView) findViewById(R.id.timlinetagslistView);
        share = (RelativeLayout) findViewById(R.id.sharelayout);
        loadingtimelinetv = (TextView) findViewById(R.id.loadingtimelinetv);
        imagecredit = (TextView) findViewById(R.id.imagecredit);
        refresh = (RelativeLayout) findViewById(R.id.refreshlayout);

        seebookmark = (TextView)findViewById(R.id.seebookmark);
        allviewshare = (TextView)findViewById(R.id.allviewshare);
        watchvideotv = (TextView)findViewById(R.id.watchvideotv);


        watch = (LinearLayout) findViewById(R.id.allviewwatchvideolayout);
        bookmarklayout = (RelativeLayout)findViewById(R.id.bookmarklayout);
        buttonlayout = (RelativeLayout) findViewById(R.id.allviewbuttonlayout);
        viewPager = (ViewPager) findViewById(R.id.allpager);
        listview = (ListView) findViewById(R.id.alllistview);

        goupbutton = (Button)findViewById(R.id.goupbutton);
        setbookmark = (ImageView)findViewById(R.id.setbookmark);
        backbookmark = (ImageView)findViewById(R.id.backbookmark);
        moreonbutton = (Button)findViewById(R.id.moreonbutton);

        all = (LinearLayout) findViewById(R.id.allmenu);
        science = (LinearLayout) findViewById(R.id.sciencemenu);
        entertainment = (LinearLayout) findViewById(R.id.entertainmentmenu);
        simplified = (LinearLayout) findViewById(R.id.simplifiedmenu);
        viral = (LinearLayout) findViewById(R.id.viralmenu);
        india = (LinearLayout) findViewById(R.id.indiamenu);
        sports = (LinearLayout) findViewById(R.id.sportsmenu);
        business = (LinearLayout) findViewById(R.id.businessmenu);
        world = (LinearLayout) findViewById(R.id.worldmenu);

        infolayout = (LinearLayout) findViewById(R.id.infolayout);
        imagecreditlayout = (LinearLayout) findViewById(R.id.imagecreditlayout);
        youareoffline = (TextView)findViewById(R.id.youareoffline);
        tagslayout = (RelativeLayout)findViewById(R.id.tagslayout);
        tagsselectionimageview = (ImageView)findViewById(R.id.tagsselectionimageview);
    }

    private void removeBorder()
    {
        all.setBackground(getResources().getDrawable(R.drawable.remove_border));
        science.setBackground(getResources().getDrawable(R.drawable.remove_border));
        entertainment.setBackground(getResources().getDrawable(R.drawable.remove_border));
        sports.setBackground(getResources().getDrawable(R.drawable.remove_border));
        viral.setBackground(getResources().getDrawable(R.drawable.remove_border));
        business.setBackground(getResources().getDrawable(R.drawable.remove_border));
        india.setBackground(getResources().getDrawable(R.drawable.remove_border));
        world.setBackground(getResources().getDrawable(R.drawable.remove_border));
        simplified.setBackground(getResources().getDrawable(R.drawable.remove_border));
    }

    private void removeOptions()
    {
        if (buttonlayout.getVisibility() == View.VISIBLE)
        {
            imagecreditlayout.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(
                    AllCategory.this, R.anim.slide_out_to_bottom
            );
            Animation animation1 = AnimationUtils.loadAnimation(
                    AllCategory.this, R.anim.slide_out_up
            );
            buttonlayout.setAnimation(animation);
            bookmarklayout.setAnimation(animation1);
            buttonlayout.setVisibility(View.GONE);
            bookmarklayout.setVisibility(View.GONE);
        }
    }

    private void resetViewPager()
    {
        onceTime = true;
        tagsselction = "";
        fromdatabase(temp);
        settingViewPager();
        removeOptions();
        bookmarkswitcher();
    }

    private void setArraylists(int t, ArrayList<String> tempPid, ArrayList<String> tempothertags,
                               ArrayList<String> temp_id, ArrayList<String> templinktonews,
                               ArrayList<String> temptimestamplist, ArrayList<String> tempisS3lis) {
        public_idlist.add(tempPid.get(t));
        othertags.add(tempothertags.get(t));
        _id.add(temp_id.get(t));
        linktonews.add(templinktonews.get(t));
        timestamplist.add(temptimestamplist.get(t));
        isS3lis.add(tempisS3lis.get(t));
        tempPid.remove(t);
        tempothertags.remove(t);
        temp_id.remove(t);
        templinktonews.remove(t);
        temptimestamplist.remove(t);
        tempisS3lis.remove(t);
    }

    private void searchLink(ArrayList<String> tempPid, ArrayList<String> tempothertags, ArrayList<String> temp_id, String s, ArrayList<String> templinktonews, ArrayList<String> temptimestamplist, ArrayList<String> tempisS3lis) {

        for (int t= 0 ; t<temp_id.size();t++){
            if (temp_id.get(t).equals(s)) {
                if (templinktonews.get(t).equals("")) {
                    setArraylists(t, tempPid, tempothertags, temp_id, templinktonews, temptimestamplist , tempisS3lis);
                } else {
                    searchLink(tempPid, tempothertags, temp_id, templinktonews.get(t), templinktonews, temptimestamplist, tempisS3lis);
                    setArraylists(t, tempPid, tempothertags, temp_id, templinktonews, temptimestamplist, tempisS3lis);
                }break;
            }
        }
    }

    private void setBookmarkMethod()
    {
        String s0 = _id.get(currenPosition);
        String s = public_idlist.get(currenPosition);
        String s1 = timestamplist.get(currenPosition);
        String s2 = othertags.get(currenPosition);
        String s3 = linktonews.get(currenPosition);
        String s4 = isS3lis.get(currenPosition);
        SavingBookmark savingbookmark = new SavingBookmark(this);
        try
        {
            savingbookmark.open();
        }
        catch (Exception exception2)
        {
            exception2.printStackTrace();
        }

        if (savingbookmark.checkID(s0)) {
            savingbookmark.deleteLink(s0);
            setbookmark.setImageDrawable(getResources().getDrawable(R.drawable.b_no));
            youareoffline.setText("Bookmark removed.");
            youareoffline.setVisibility(View.VISIBLE);
            youareofflineC.cancel();
            youareofflineC.start();
        } else {
            try
            {
                savingbookmark.createEntry(1, s0, s, s1, s2, s3, s4);
                youareoffline.setText("Bookmarked.");
                youareoffline.setVisibility(View.VISIBLE);
                youareofflineC.cancel();
                youareofflineC.start();
            } catch (Exception exception1)
            {
                exception1.printStackTrace();
            }
            setbookmark.setImageDrawable(getResources().getDrawable(R.drawable.b_yes));
            final File file = new File((new MyDirectory()).getDirectory(), s);
            if (!file.exists())
            {
                s = (new StringBuilder()).append("http://d2vwmcbs3lyudp.cloudfront.net/").append(s).toString();
                Picasso.with(getBaseContext()).load(s).into(new Target() {
                    public void onBitmapFailed(Drawable drawable)
                    {
                    }
                    public void onBitmapLoaded(Bitmap bitmap, com.squareup.picasso.Picasso.LoadedFrom loadedfrom)
                    {
                        try
                        {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                        }
                        // Misplaced declaration of an exception variable
                        catch (Exception e)
                        {
                            Log.e("book", e.toString());
                        }
                    }

                    public void onPrepareLoad(Drawable drawable)
                    {
                    }
                });
            }
        }
        try
        {
            savingbookmark.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void setLoadingON(String s)
    {
        loadingtimelinetv.setVisibility(View.VISIBLE);
        loadingtimelinetv.setText("Loading Timeline...");
        temptags = s.split(",");
        tagsji = new ArrayList();
        tagsji.add(0, timelineon);
        for (int i = 0; i < temptags.length; i++)
        {
            tagsji.add(i + 1, temptags[i]);
            //loadingtimelinetv.append((new StringBuilder()).append("\n").append(temptags[i]).toString());
        }

    }

    private void setOthertags(int i)
    {
        if ((othertags.get(i)).equals(""))
        {
            ArrayList<String> notagsji = new ArrayList<String>();
            notagsji.add(0, notimeline);
            Tag_View tag_view = new Tag_View(getBaseContext(),notagsji);
            taglist.setAdapter(tag_view);
            tagslayout.setVisibility(View.VISIBLE);
        } else
        {
            Tag_View tag_view = new Tag_View(getBaseContext(), tagsji);
            taglist.setAdapter(tag_view);
            tagslayout.setVisibility(View.VISIBLE);
        }
    }

    private void setSwitchContent(boolean flag)
    {
        removeBorder();
        Controller.getInstance().trackEvent("All", "Menu", "user");
        Controller.getInstance().trackScreenView("All");
        isviral = false;
        currentCategory = "ALL";
        try
        {
            SavingPublicId savingpublicid = new SavingPublicId(this);
            savingpublicid.open();
            temp.removeAll(temp);
            temp = savingpublicid.getAll();
            savingpublicid.close();
        }
        catch (Exception exception) { }
        resetViewPager();

    }

    private void setVisiblityofwatchbutton()
    {
        if (youtubelink.equals(null) || youtubelink.equals(""))
        {
            watch.setVisibility(View.GONE);
        } else {
            watch.setVisibility(View.VISIBLE);
            Log.e("out", String.valueOf(videopost));
            if (!videopost)
            {
                if (videopostCounter < 1)
                {
                    countit.start();
                }
                editor.putBoolean("videopost", true);
                videopost = true;
                editor.commit();
                editor.apply();
            }
        }
    }

    private void settingViewPager()
    {
        adapter = new FullScreenImageAdapter(this,
                public_idlist,
                getResources(),
                this,
                buttonlayout,
                isviral,
                timestamplist,
                imagecreditlayout,
                isS3lis,
                bookmarklayout);

        viewPager.setAdapter(adapter);
        if (public_idlist.size()>0) {
            if (public_idlist.size()==1){
                new ExtendCategory().execute();
            }
            if (moveto.equals("")) {
                viewPager.setCurrentItem(currenPosition);
                name = public_idlist.get(currenPosition);
                //setOthertags(currenPosition);
            } else {
                for (int move = 0; move < public_idlist.size(); move++) {
                    if (public_idlist.get(move).equals(moveto)) {
                        viewPager.setCurrentItem(move);
                        name = public_idlist.get(move);
                        moveto="";
                        break;
                    }
                }
            }

        }else {
            new ExtendCategory().execute();
        }
        viewPager.setOffscreenPageLimit(3);
        //Log.e("Name ", name);
        Controller.getInstance().trackScreenView(getBaseContext().getResources().getString(R.string.url) + name + ".png");


        getLink();
        setVisiblityofwatchbutton();
    }

    public void getLink()
    {
        try
        {
            SavingYoutubeLink savingyoutubelink = new SavingYoutubeLink(this);
            savingyoutubelink.open();
            youtubelink = savingyoutubelink.get_youtubeVideoId(name);
            savingyoutubelink.close();
            return;
        }
        catch (Exception exception)
        {
            return;
        }
    }

    public void onBackPressed()
    {
        if (tagslayout.getVisibility() == View.VISIBLE)
        {
            tagslayout.setVisibility(View.GONE);
        }
        if (moreonbutton.getVisibility() == View.VISIBLE)
        {
            pager.setCurrentPage(0);
        }
        if (close2.getVisibility() == View.VISIBLE)
        {
            if (viewPager.getCurrentItem() > 0)
            {
                close2.setVisibility(View.GONE);
            } else
            {
                closeApp();
            }
        }
        if (viewPager.getCurrentItem() <= 0)
        {
            if (currentCategory == "ALL")
            {
                closeApp();
            } else
            {
                callMenu();
                removeOptions();
            }
        } else
        {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {

            case R.id.backbookmark:
                callMenu();
                removeOptions();
                break;
            case R.id.refreshlayout:
                callMenu();
                removeOptions();
                break;

            case R.id.seebookmark:
                SavingBookmark savingBookmark = new SavingBookmark(this);
                try {
                    savingBookmark.open();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (savingBookmark.getTotal() > 0) {
                    backbookmark.setVisibility(View.VISIBLE);
                    temp = savingBookmark.getAll();
                    isviral = false;
                    currentCategory = mycollection;
                    currenPosition = 0;
                    moveto = "";
                    resetViewPager();
                    bookmarklayout.setVisibility(View.VISIBLE);
                } else {
                    youareoffline.setText("No Bookmark marked yet.");
                    youareoffline.setVisibility(View.VISIBLE);
                    youareofflineC.cancel();
                    youareofflineC.start();
                }
                try {
                    savingBookmark.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                removeOptions();
                return;

            case R.id.setbookmark:
                if (CheckNetworkConnection.isConnectionAvailable(getBaseContext())) {
                    setBookmarkMethod();
                    return;
                } else {
                    youareoffline.setVisibility(View.VISIBLE);
                    youareofflineC.cancel();
                    youareofflineC.start();
                    return;
                }

            case R.id.tagslayout:
                tagslayout.setVisibility(View.GONE);
                break;

            case R.id.moreonbutton:
                setOthertags(currenPosition);
                break;

            case R.id.tagsselectionimageview:
                if (tagsselction.equals("")) {
                    tagslayout.setVisibility(View.GONE);
                }else {
                    new FetchTimeline(false).execute();
                    loadingtimelinetv.setVisibility(View.VISIBLE);
                    loadingtimelinetv.setText("Loading Timeline on:");
                    String[] tempselectedtags = tagsselction.substring(0, tagsselction.length() - 1).split(",");
                    for (int i = 0; i < tempselectedtags.length; i++) {
                        loadingtimelinetv.append((new StringBuilder()).append("\n").append(tempselectedtags[i]).toString());
                    }

                    tagslayout.setVisibility(View.GONE);
                }
                break;
            case R.id.sharelayout:
                new ShareImageTask(this, name);
                break;

            case R.id.allviewwatchvideolayout:
                Intent i = new Intent(this,VideoPlayer.class);
                i.putExtra("watch", youtubelink);
                i.putStringArrayListExtra("youtubeVideoId",new ArrayList<String>());
                startActivity(i);
                break;

            case  R.id.goupbutton:
                pager.setCurrentPage(0);
                break;

            case R.id.infolayout:
                imagecreditlayout.setVisibility(View.VISIBLE);
                try {
                    SavingPublicId savingPublicId = new SavingPublicId(AllCategory.this);
                    savingPublicId.open();
                    String crts = savingPublicId.getCredits(public_idlist.get(currenPosition));
                    savingPublicId.close();
                    imagecredit.setText(Html.fromHtml("<b>Image Courtesy- </b>" + crts));
                } catch (Exception e) {
                    imagecredit.setText(Html.fromHtml("<b>Image Courtesy- </b>"));
                }

                break;
        }
    }


    protected void onPause()
    {
        super.onPause();
        if (!isviral)
        {
            timer.start();
        }
    }

    protected void onResume()
    {
        super.onResume();
        timer.cancel();
    }

    public void setListViewHeightBasedOnChildren(ListView listview1, int i)
    {
        ListAdapter listadapter = listview1.getAdapter();
        if (listadapter == null)
        {
            return;
        }
        if (listadapter.getCount() < 0)
        {
            View view = listadapter.getView(0, null, listview1);
            view.measure(0, 0);
            view.getMeasuredHeight();
        }
        android.view.ViewGroup.LayoutParams layoutparams = listview1.getLayoutParams();
        float f = getResources().getDisplayMetrics().density;
        i /= 160;
        layoutparams.height = (int)(151F * f) * listadapter.getCount() + (int)(200F * f);
        listview1.setLayoutParams(layoutparams);
        listview1.requestLayout();
    }

    public void showCustomAlert()
    {
        youareoffline.setVisibility(View.VISIBLE);
        youareofflineC.cancel();
        youareofflineC.start();
    }

    private class ExtendCategory extends AsyncTask<String,String,String> {

        String gettime;
        SavingPublicId savingPublicId;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            savingPublicId = new SavingPublicId(AllCategory.this);
            try
            {
                savingPublicId.open();
            }
            catch (Exception exception) { }
            if (public_idlist.size() > 0)
            {
                String s = timestamplist.get(public_idlist.size() - 1);
                Log.e("timeStamp ", s);
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                simpledateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    gettime = String.valueOf(simpledateformat.parse(s).getTime());
                }
                catch (ParseException parseexception)
                {
                    parseexception.printStackTrace();
                }
            } else
            {
                gettime = "-1";
            }
            Log.e(gettime, String.valueOf(public_idlist.size() - 1));
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                String strUrl = getResources().getString(R.string.apiurl) + "/api/v1/news/category/" + currentCategory + "/feed/" + gettime + "?apiKey=" + gettoken;
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
                result = sb.toString();
                Log.e("pass 2", "connection success ");
                JSONObject json_data = new JSONObject(result);
                JSONArray array = json_data.getJSONArray("data");
                SavingYoutubeLink savingYoutubeLink;
                savingYoutubeLink = new SavingYoutubeLink(AllCategory.this);
                savingYoutubeLink.open();
                if (array.length()>0) {
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);
                        JSONObject publish = data.getJSONObject("publish");
                        JSONObject portrait = publish.getJSONObject("portrait");
                        JSONObject url_id = portrait.getJSONObject("url");
                        JSONObject attributes = data.getJSONObject("attributes");
                        JSONObject source = data.getJSONObject("source");
                        JSONObject image = source.getJSONObject("image");
                        JSONObject tags = data.getJSONObject("tags");
                        JSONArray others = tags.getJSONArray("other");
                        String othert = "";
                        for (int j = 0; j < others.length(); j++) {
                            String temptag = others.getString(j);
                            if (others.length() == 1) {
                                othert += temptag;
                            } else if (j == 0) {
                                othert += temptag;
                            } else if (j == (others.length() - 1)) {
                                othert += "," + temptag;
                            } else {
                                othert += "," + temptag;

                            }
                        }
                        JSONArray people = tags.getJSONArray("people");
                        for (int j = 0; j < people.length(); j++) {
                            String temptag = people.getString(j);
                            othert += "," + temptag;
                        }
                        JSONArray place = tags.getJSONArray("place");
                        for (int j = 0; j < place.length(); j++) {
                            String temptag = place.getString(j);
                            othert += "," + temptag;
                        }
                        Log.e("Tagi", othert);
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
                        String heading = data.getString("headline");
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
                        try {
                            isFact = String.valueOf(tags.getBoolean("isFact"));
                        } catch (Exception e) {

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
                        temp.add(p_id);
                        temp.add(othert);
                        temp.add(_id);
                        temp.add(linkedToNews);
                        temp.add(timestamp);
                        temp.add(isS3);
                        //Saving Recent Posts
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
                                othert,
                                linkedToNews,
                                isFact,
                                heading, isS3, creditname);
                    }

                }else {
                }
                savingYoutubeLink.close();
            }catch (Exception e){
                Log.e("Ex ",e.toString());
            }
            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (temp.size() > 0)
            {
                Log.e("Size", String.valueOf(temp.size()));
                resetViewPager();
            }
            try
            {
                savingPublicId.close();
            }
            // Misplaced declaration of an exception variable
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }





    }

    private class FetchTimeline extends AsyncTask<String,String,String> {

        private final boolean b;
        private Custom_view cv;
        public FetchTimeline(boolean flag) {
            b = flag;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            headlines.removeAll(headlines);
            youtubeVideoId.remove(youtubeVideoId);
            contents.removeAll(contents);
            timelinedate.removeAll(timelinedate);
            timelinepublicid.removeAll(timelinepublicid);
            timelines3.remove(timelines3);
            timelinecredits.removeAll(timelinecredits);
            cv = new Custom_view(getBaseContext(),
                    headlines,
                    contents,
                    timelinedate,
                    timelinepublicid,
                    getResources(),
                    AllCategory.this,
                    timelines3,
                    timelinecredits,
                    name,
                    youtubeVideoId);
            listview.setAdapter(cv);
        }
        @Override
        protected String doInBackground(String... params) {

            getting_id = _id.get(currenPosition);
            String _idl = getting_id;
            InputStream is = null;
            String line;

            try {
                String strUrl;
                if (b) {
                    strUrl = (new StringBuilder()).append(getResources().getString(R.string.apiurl))
                            .append("/api/v1/news/related/")
                            .append(_idl).append("?apiKey=")
                            .append(gettoken).toString();
                } else {
                    String stags = tagsselction.substring(0, tagsselction.length() - 1);
                    String fstags = (new StringBuilder()).append("%5B%22")
                            .append(stags.replaceAll(",", "%22%2C%22"))
                            .append("%22%5D").toString();
                    strUrl = (new StringBuilder()).append(getResources().getString(R.string.apiurl))
                            .append("/api/v1/news/relatedBytag/")
                            .append(fstags).append("?apiKey=")
                            .append(gettoken).toString();
                }
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
                    if(!tags.getString("category").equals("VIRAL")) {
                        String h = data.getString("headline");
                        headlines.add( h);
                        String com = content.getString("html");
                        contents.add( com);
                        String p_id = "";
                        try {
                            p_id = url_id.getString("public_id");
                            timelines3.add(false);
                        } catch (Exception e) {
                            p_id = url_id.getString("filename");
                            timelines3.add(true);
                        }
                        timelinepublicid.add(p_id);
                        timelinecredits.add(image.getString("name"));
                        timelinedate.add( data.getString("timestampCreated"));
                        youtubeVideoId.add(data.getString("youtubeVideoId"));
                    }

                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            cv.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listview, height);
            listview.setVisibility(View.VISIBLE);
            loadingtimelinetv.setVisibility(View.GONE);
        }
    }

    public class Tag_View extends ArrayAdapter {

        private final Context context;
        private final ArrayList<String> tagget;
        TextView tags;

        public Tag_View(Context context,
                        ArrayList<String> tagget) {

            super(context, R.layout.timeline_layout, tagget);
            this.context = context;
            this.tagget = tagget;
            if (tagsselction.equals("")) {
                tagsselectionimageview.setImageDrawable(getResources().getDrawable(R.drawable.timelineclose));
            } else {
                tagsselectionimageview.setImageDrawable(getResources().getDrawable(R.drawable.viralgreen));
            }
        }

        public View getView(final int view_pos, View convertView, ViewGroup parent) {
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View rowView = inflater.inflate(R.layout.timeline_tags, parent, false);
                tags = (TextView) rowView.findViewById(R.id.tags);
                String item_tag = tagget.get(view_pos);
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "lodingfont.ttf");
                tags.setTypeface(typeface);
                tags.setText(item_tag);
                if (tagsselction.contains(item_tag)) {
                    tags.setBackgroundColor(Color.parseColor("#f7941e"));
                    tags.setTextColor(Color.BLACK);
                } else {
                    tags.setBackgroundColor(Color.BLACK);
                    tags.setTextColor(Color.WHITE);
                }
                final String finalitem_tag = item_tag;
                final TextView finaltags = tags;
                rowView.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        if (!finalitem_tag.equals(timelineon) && !finalitem_tag.equals(notimeline)) {
                            if (tagsselction.contains(finalitem_tag)) {
                                finaltags.setBackgroundColor(0xff000000);
                                finaltags.setTextColor(Color.WHITE);
                                tagsselction = tagsselction.replaceAll((new StringBuilder()).append(finalitem_tag).append(",").toString(), "");
                            } else {
                                tagsselction = (new StringBuilder()).append(tagsselction).append(finalitem_tag).append(",").toString();
                                finaltags.setBackgroundColor(Color.parseColor("#f7941e"));
                                finaltags.setTextColor(Color.BLACK);
                            }
                        }
                        Log.e("tagsselction", tagsselction);
                        if (tagsselction.equals("")) {
                            tagsselectionimageview.setImageDrawable(getResources().getDrawable(R.drawable.timelineclose));
                            return;
                        } else {
                            tagsselectionimageview.setImageDrawable(getResources().getDrawable(R.drawable.viralgreen));
                            return;
                        }
                    }
                });
                return rowView;
            }


        }

    }
    CountDownTimer youareofflineC = new CountDownTimer(2*1000, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {
            //Logout
            youareoffline.setVisibility(View.GONE);
            //youareoffline.setBackgroundResource(Color.parseColor("#000000"));
            youareoffline.setText(getResources().getString(R.string.interneterror));
        }
    };

    CountDownTimer timer = new CountDownTimer(5 *60 * 1000, 5000) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {
            //Logout
            finish();
        }
    };

    CountDownTimer countit = new CountDownTimer(1* 1000, 500) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {
            //Logout
            //new FetchTimeline().execute();
            final LinearLayout watchvideo = (LinearLayout) findViewById(R.id.watchvideotut);
            watchvideo.setVisibility(View.VISIBLE);
            watchvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchvideo.setVisibility(View.GONE);

                }
            });
        }

    };

}
