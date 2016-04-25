// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

package best.news.shorts.app.glance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import best.news.shorts.app.glance.ryanharter.viewpager.ViewPager;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

// Referenced classes of package best.news.shorts.app.glance:
//            Timeline_adapter, ReturnColor, Controller

public class Viral_ListView extends ArrayAdapter
{

    private final ArrayList<String> youtubeVideoId;
    private final ArrayList<String> timelinetags;
    private final Resources getres;
    private final int width;
    private final int height;
    private final ArrayList<String> _id;
    private final ArrayList<String> viraltimestampcreated;
    private final ProgressBar progressBarforlist;
    private final ArrayList<String> timelinepeopletags;
    private final ArrayList<String> timelineplacetags;
    private Activity _activity;
    private final Context context;
    private final ArrayList<String> headlines;
    private final ArrayList<String> timelinedate;
    private final ArrayList<String> timelinepublicid;
    private TextView virallistviewduration;
    private TextView virallistviewtitle;
    private RelativeLayout virallistviewimageviewlayout;
    private Button viralbookmark;
    private RelativeLayout virallistviewtitlelayout;
    private ImageView virallistviewimageview;
    private GridLayout gridlayouttags;


    public Viral_ListView(Context context1,
                          ArrayList<String> headlines,
                          ArrayList<String> timelinedate,
                          ArrayList<String> timelinepublicid,
                          Resources resources,
                          Activity activity,
                          ArrayList<String> timelinetags,
                          ArrayList<String> youtubeVideoId,
                          ArrayList<String> _id,
                          ArrayList<String> viraltimestampcreated,
                          ProgressBar progressBarforlist,
                          ArrayList<String> timelinepeopletags,
                          ArrayList<String> timelineplacetags
                          )
    {
        super(context1, R.layout.timeline_layout, headlines);
        context = context1;
        getres = resources;
        _activity = activity;
        this.headlines = headlines;
        this.timelinedate = timelinedate;
        this.timelinepublicid = timelinepublicid;
        this.timelinetags = timelinetags;
        this.youtubeVideoId =youtubeVideoId;
        this._id = _id;
        this.progressBarforlist =progressBarforlist;
        this.viraltimestampcreated =viraltimestampcreated;
        this.timelinepeopletags = timelinepeopletags;
        this.timelineplacetags =timelineplacetags;
        Drawable draw = getres.getDrawable(R.drawable.viralprogressbar);
        this.progressBarforlist.setProgressDrawable(draw);
        this.progressBarforlist.setProgress(0);
        Display display = _activity.getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
    }

    public View getView(final int view_pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.viral_listview, parent, false);

        virallistviewduration = (TextView) rowView.findViewById(R.id.virallistviewduration);
        virallistviewtitle = (TextView) rowView.findViewById(R.id.virallistviewtitle);
        virallistviewimageviewlayout = (RelativeLayout) rowView.findViewById(R.id.virallistviewimageviewlayout);
        viralbookmark = (Button) rowView.findViewById(R.id.viralbookmark);
        virallistviewtitlelayout = (RelativeLayout) rowView.findViewById(R.id.virallistviewtitlelayout);
        virallistviewimageview = (ImageView) rowView.findViewById(R.id.virallistviewimageview);
        gridlayouttags = (GridLayout) rowView.findViewById(R.id.gridlayouttags);


        final String item_headline = headlines.get(view_pos);
        final String item_public_id = timelinepublicid.get(view_pos);
        final String item_duration = timelinedate.get(view_pos);
        final String item_tags = timelinetags.get(view_pos);
        final String item_peopletags = timelinepeopletags.get(view_pos);
        final String item_placetags = timelineplacetags.get(view_pos);
        String item__id = _id.get(view_pos);
        final String item_viraltimestampcreated = viraltimestampcreated.get(view_pos);
        final String item_youtubeVideoId = youtubeVideoId.get(view_pos);

        //gridlayouttags.setUseDefaultMargins(true);
        Typeface lodingfont = Typeface.createFromAsset(_activity.getAssets(), "lodingfont.ttf");
        Typeface content = Typeface.createFromAsset(_activity.getAssets(), "content.otf");
        virallistviewtitle.setTypeface(lodingfont);
        virallistviewduration.setTypeface(content);
        String imgageUrl = "http://d2vwmcbs3lyudp.cloudfront.net/" + item_public_id;


    if (!item_tags.equals("")) {
    String[] tags = item_tags.split(",");
        for (int i = 0; i < tags.length; i++) {
            final TextView tagtv = new TextView(_activity);
            tagtv.setText(tags[i]);
            tagtv.setTypeface(lodingfont);
            //tagtv.setPadding(7,7,7,7);
            tagtv.setTextSize(16);
            tagtv.setBackground(getres.getDrawable(R.drawable.othertagborder));
            tagtv.setTextColor(Color.parseColor("#754c29"));
            tagtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tagsselction=textView.getText().toString();
                    //new FetchTimeline().execute();
                    Intent i = new Intent(_activity, ViralList.class);
                    i.putExtra("tag", tagtv.getText().toString());
                    _activity.startActivity(i);
                }
            });
            gridlayouttags.addView(tagtv);
        }
    }
    if (!item_peopletags.equals("")) {

    String[] pltags = item_peopletags.split(",");
        for (int i = 0; i < pltags.length; i++) {
            final TextView tagtv = new TextView(_activity);
            tagtv.setText(pltags[i]);
            tagtv.setTypeface(lodingfont);
            //tagtv.setPadding(7,7,7,7);
            tagtv.setTextSize(16);
            tagtv.setBackground(getres.getDrawable(R.drawable.placeborder));
            tagtv.setTextColor(Color.parseColor("#ec008c"));
            tagtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tagsselction=textView.getText().toString();
                    //new FetchTimeline().execute();
                    Intent i = new Intent(_activity, ViralList.class);
                    i.putExtra("tag", tagtv.getText().toString());
                    _activity.startActivity(i);
                }
            });
            gridlayouttags.addView(tagtv);
        }
        }
        if (!item_placetags.equals("")) {
            String[] petags = item_placetags.split(",");
            for (int i = 0; i < petags.length; i++) {
                final TextView tagtv = new TextView(_activity);
                tagtv.setText(petags[i]);
                tagtv.setTypeface(lodingfont);
                //tagtv.setPadding(7,7,7,7);
                tagtv.setTextSize(16);
                tagtv.setBackground(getres.getDrawable(R.drawable.peopleborder));
                tagtv.setTextColor(Color.parseColor("#00aeef"));
                tagtv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //tagsselction=textView.getText().toString();
                        //new FetchTimeline().execute();
                        Intent i = new Intent(_activity, ViralList.class);
                        i.putExtra("tag", tagtv.getText().toString());
                        _activity.startActivity(i);
                    }
                });
                gridlayouttags.addView(tagtv);
            }
        }
        Picasso.with(context)
                .load(imgageUrl)
                .resize(width, width/2)
                .into(virallistviewimageview);
        virallistviewtitle.setText(item_headline);
        virallistviewduration.setText(item_duration);


        try {
            SavingViral savingViral = new SavingViral(_activity);
            savingViral.open();
            if (savingViral.getData(item__id)){
                viralbookmark.setBackground(getres.getDrawable(R.drawable.b_yes));
            }else {
                viralbookmark.setBackground(getres.getDrawable(R.drawable.b_no));

            }
        }catch (Exception e){

        }



        virallistviewimageviewlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(_activity,VideoPlayer.class);
                k.putExtra("watch", item_youtubeVideoId);
                k.putStringArrayListExtra("youtubeVideoId",youtubeVideoId);
                _activity.startActivity(k);
            }
        });

        final String finalitem__id = item__id;
        final RelativeLayout finalvirallistviewtitlelayout = virallistviewtitlelayout;
        final Button finalviralbookmark = viralbookmark;

        finalvirallistviewtitlelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavingViral savingViral = new SavingViral(_activity);
                try {
                    savingViral.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (savingViral.getData(finalitem__id)){
                    savingViral.deletePost(finalitem__id);
                    finalviralbookmark.setBackground(getres.getDrawable(R.drawable.b_no));
                }else {
                    try {
                        savingViral.createEntry(
                                item_headline,
                                item_public_id,
                                item_duration,
                                item_tags,
                                finalitem__id,
                                item_viraltimestampcreated,
                                item_youtubeVideoId,
                                item_peopletags,item_placetags
                        );
                        finalviralbookmark.setBackground(getres.getDrawable(R.drawable.b_yes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    savingViral.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        if (view_pos==0)
        progressBarforlist.setMax(headlines.size());
        progressBarforlist.setProgress(view_pos+1);
        return rowView;
    }



}
