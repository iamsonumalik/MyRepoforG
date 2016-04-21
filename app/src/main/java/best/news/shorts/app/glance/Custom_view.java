// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import best.news.shorts.app.glance.ryanharter.viewpager.ViewPager;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

// Referenced classes of package best.news.shorts.app.glance:
//            Timeline_adapter, ReturnColor, Controller

public class Custom_view extends ArrayAdapter
{

    private final ArrayList<String> youtubeVideoId;
    private Activity _activity;
    Button abovebox;
    private final Timeline_adapter adapter;
    Button belowbox;
    LinearLayout bottomlayout;
    LinearLayout box;
    private final ArrayList<String> contents;
    TextView contentstv;
    private final Context context;
    TextView datetv;
    private File directory;
    private Resources getres;
    private final ArrayList<String> headlines;
    TextView headlinestv;
    private final int height;
    private final String name;
    private int prev;
    ReturnColor returnColor;
    private final ArrayList<String> timelinedate;
    LinearLayout timelinelayout;
    private ProgressBar timelineprogressbar;
    private final ArrayList<String> timelinepublicid;
    private final ArrayList<Boolean> timelines3;
    private final int width;
    private TextView endoftimeline;

    public Custom_view(Context context1,
                       ArrayList<String> arraylist,
                       ArrayList<String> arraylist1,
                       ArrayList<String> arraylist2,
                       ArrayList<String> arraylist3,
                       Resources resources,
                       Activity activity,
                       ArrayList<Boolean> arraylist4,
                       ArrayList<String> arraylist5,
                       String s, ArrayList<String> youtubeVideoId)
    {
        super(context1, R.layout.timeline_layout, arraylist);
        directory = null;
        context = context1;
        getres = resources;
        _activity = activity;
        contents = arraylist1;
        headlines = arraylist;
        timelinedate = arraylist2;
        timelinepublicid = arraylist3;
        timelines3 = arraylist4;
        name = s;
        this.youtubeVideoId =youtubeVideoId;
        adapter = new Timeline_adapter(activity, arraylist3, context1, arraylist2, arraylist4, arraylist5, headlines, contents,youtubeVideoId);
        returnColor = new ReturnColor();
        prev = 0;
        Display display = _activity.getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
        if (Environment.getExternalStorageState() == null)
        {
            directory = new File((new StringBuilder()).append(Environment.getDataDirectory())
                    .append("/Android/data/best.news.shorts.app.glance/cache").toString());
            if (!directory.exists())
            {
                directory.mkdirs();
            }
        } else
        if (Environment.getExternalStorageState() != null)
        {
            directory = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory())
                    .append("/Android/data/best.news.shorts.app.glance/cache").toString());
            if (!directory.exists())
            {
                directory.mkdirs();
                return;
            }
        }
    }

    public View getView(final int view_pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.timeline_layout, parent, false);
        datetv = (TextView) rowView.findViewById(R.id.timelinedatetv);
        headlinestv = (TextView) rowView.findViewById(R.id.timelineTitletv);
        contentstv = (TextView) rowView.findViewById(R.id.timelinecontenettv);
        timelinelayout = (LinearLayout) rowView.findViewById(R.id.timelinelayout);

        endoftimeline = (TextView) rowView.findViewById(R.id.endoftimeline);
        abovebox = (Button) rowView.findViewById(R.id.smalllineabovetimline);
        belowbox = (Button) rowView.findViewById(R.id.smalllinebelowtimline);
        box = (LinearLayout) rowView.findViewById(R.id.leftstyle);
        bottomlayout = (LinearLayout) rowView.findViewById(R.id.bottomlayout);
        timelineprogressbar = (ProgressBar) rowView.findViewById(R.id.timelineprogressbar);
        int col = (view_pos)%9;
        datetv.setTextColor(getres.getColor(returnColor.colorint(col)));
        box.setBackgroundResource(returnColor.colorint(col));
        belowbox.setBackgroundResource(returnColor.colorint(col));
        timelinelayout.setVisibility(View.VISIBLE);
        String item_content="";
        String item_headline="";
        String item_timeline_public_id="";
        String item_timeline_date="";
        try {
            item_content = contents.get(view_pos);
            item_headline = headlines.get(view_pos);
            item_timeline_public_id = timelinepublicid.get(view_pos);
            item_timeline_date = timelinedate.get(view_pos);
        }catch (Exception e){

        }
        Typeface cont = Typeface.createFromAsset(_activity.getAssets(), "content.otf");
        Typeface head = Typeface.createFromAsset(_activity.getAssets(), "headline.otf");
        headlinestv.setText(item_headline);
        contentstv.setText(item_content);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("d MMM yyyy");
            String dateget = item_timeline_date;

            date = sdf.parse(dateget);

            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDate = dateFormatGmt.format(date);
            datetv.setText(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        headlinestv.setTypeface(head);
        contentstv.setTypeface(cont);
        datetv.setTypeface(cont);
        endoftimeline.setTypeface(head);

        final String finalItem_timeline_public_id = item_timeline_public_id;
        timelinelayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1)
            {
                Controller.getInstance().trackEvent("TimelineClicked", finalItem_timeline_public_id, "user");
                final Dialog dialog = new Dialog(_activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.timeline_dialog);
                final ViewPager show = (ViewPager) dialog.findViewById(R.id.timelinedialogimageView);
                FrameLayout close = (FrameLayout) dialog.findViewById(R.id.timelinedialogbackground);
                final ImageView timelineguide = (ImageView) dialog.findViewById(R.id.timelineguide);
                final TranslateAnimation animation = new TranslateAnimation(0.0f,0.0f,0.0f,-50.0f);
                animation.setDuration(1500);
                //animation.setRepeatMode(2);
                animation.setRepeatCount(2);
                timelineguide.startAnimation(animation);
                Button closetimelinedialog = (Button) dialog.findViewById(R.id.closetimelinedialog);

                show.setAdapter(adapter);
                show.setCurrentItem(view_pos);

                if (view_pos==(headlines.size()-1))
                {
                    timelineguide.clearAnimation();
                    timelineguide.setVisibility(View.GONE);
                }
                show.setOnPageChangeListener (new best.news.shorts.app.glance.ryanharter.viewpager.ViewPager.OnPageChangeListener() {

                    public void onPageScrollStateChanged(int i)
                    {
                    }

                    public void onPageScrolled(int i, float f, int j)
                    {
                    }

                    public void onPageSelected(int i)
                    {
                        timelineguide.clearAnimation();
                        timelineguide.setVisibility(View.GONE);
                    }

                });
                    closetimelinedialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });
        if (finalItem_timeline_public_id.equals(name))
        {
            headlinestv.setBackgroundColor(Color.parseColor("#eca140"));
            headlinestv.setPadding(10, 10, 3, 3);
            contentstv.setVisibility(8);
            datetv.setTextColor(0xff000000);
            box.setBackgroundColor(0xff000000);
            belowbox.setBackgroundColor(0xff000000);
            if (view_pos == 0)
            {
                abovebox.setBackgroundColor(0xff000000);
            }
        }
        if (item_timeline_public_id.equals(name)){
            headlinestv.setBackgroundColor(Color.parseColor("#eca140"));
            headlinestv.setPadding(10, 10, 3, 3);
            contentstv.setVisibility(View.GONE);
            datetv.setTextColor(Color.BLACK);
            box.setBackgroundColor(Color.BLACK);
            belowbox.setBackgroundColor(Color.BLACK);
            if (view_pos==0){
                abovebox.setBackgroundColor(Color.BLACK);
            }
        }else {

        }
        if (view_pos == 0 && headlines.size() == 1) {

            belowbox.setVisibility(View.GONE);
            abovebox.setVisibility(View.VISIBLE);

        } else if (view_pos == 0) {
            abovebox.setVisibility(View.VISIBLE);

        } else if (view_pos == headlines.size()-1){
            belowbox.setBackgroundResource(returnColor.colorint(col));
            belowbox.setVisibility(View.GONE);
            bottomlayout.setVisibility(View.VISIBLE);
        }

        return rowView;
    }



}
