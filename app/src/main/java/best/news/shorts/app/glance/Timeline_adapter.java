// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import best.news.shorts.app.glance.ryanharter.viewpager.PagerAdapter;
import best.news.shorts.app.glance.ryanharter.viewpager.ViewPager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

// Referenced classes of package best.news.shorts.app.glance:
//            SavingYoutubeLink, MyDirectory, SavingCache, VideoPlayer, 
//            ShareImageTask

public class Timeline_adapter extends PagerAdapter
{

    private final ArrayList<String> youtubeVideoId;
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private final ArrayList<String> contents;
    private final Context getcontext;
    private final ArrayList<String> headlines;
    private LayoutInflater inflater;
    private final ArrayList<String> timelinecredits;
    private final ArrayList<Boolean> timelines3;
    private final ArrayList<String> timestamplist;

    public Timeline_adapter(Activity activity,
                            ArrayList<String> arraylist,
                            Context context,
                            ArrayList<String> arraylist1,
                            ArrayList<Boolean> arraylist2,
                            ArrayList<String> arraylist3,
                            ArrayList<String> arraylist4,
                            ArrayList<String> arraylist5, ArrayList<String> youtubeVideoId)
    {
        _activity = activity;
        _imagePaths = arraylist;
        timestamplist = arraylist1;
        getcontext = context;
        timelines3 = arraylist2;
        timelinecredits = arraylist3;
        headlines = arraylist4;
        contents = arraylist5;
        this.youtubeVideoId =youtubeVideoId;
    }

    public void destroyItem(ViewGroup viewgroup, int i, Object obj)
    {
        ((ViewPager)viewgroup).removeView((RelativeLayout)obj);
    }

    public int getCount()
    {
        return _imagePaths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final ImageView imgDisplay;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.timeline_viewpageradapter, container,
                false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.timelineadapterimageView);

        RelativeLayout dialogsharelayout = (RelativeLayout)viewLayout.findViewById(R.id.dialogsharelayout);
        TextView viewpageadapterdatecontent = (TextView)viewLayout.findViewById(R.id.viewpageadapterdatecontent);
        TextView viewpageadapterdateheadline = (TextView)viewLayout.findViewById(R.id.viewpageadapterdateheadline);
        TextView viewpageadapterdate = (TextView)viewLayout.findViewById(R.id.viewpageadapterdate);
        TextView viewpageadapterdateloading= (TextView)viewLayout.findViewById(R.id.viewpageadapterdateloading);

        LinearLayout dialogviewwatchvideolayout = (LinearLayout)viewLayout.findViewById(R.id.dialogviewwatchvideolayout);
        final RelativeLayout dialogviewbuttonlayout = (RelativeLayout)viewLayout.findViewById(R.id.dialogviewbuttonlayout);
        final LinearLayout dialogimagecreditlayout = (LinearLayout)viewLayout.findViewById(R.id.dialogimagecreditlayout);
        LinearLayout dialoginfolayout = (LinearLayout)viewLayout.findViewById(R.id.dialoginfolayout);
        TextView dialogimagecredit = (TextView) viewLayout.findViewById(R.id.dialogimagecredit);


        dialogimagecreditlayout.setVisibility(View.GONE);
        dialogviewbuttonlayout.setVisibility(View.GONE);
        String imagename = _imagePaths.get(position);
        String timestamp =  timestamplist.get(position);
        String head = headlines.get(position);
        String content = contents.get(position);
        boolean isS3 = timelines3.get(position);
        String credits = timelinecredits.get(position);
        String youtubelink = youtubeVideoId.get(position);


        Typeface typeface = Typeface.createFromAsset(_activity.getAssets(), "headline.otf");
        viewpageadapterdateheadline.setTypeface(typeface);
        viewpageadapterdate.setTypeface(typeface);
        viewpageadapterdateloading.setTypeface(typeface);
        dialogimagecredit.setTypeface(typeface);
        viewpageadapterdatecontent.setTypeface(Typeface.createFromAsset(_activity.getAssets(), "content.otf"));


        viewpageadapterdateheadline.setText(head);
        viewpageadapterdatecontent.setText(content);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try
        {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("d MMM yyyy");
            Date date = sdf.parse(timestamp);
            simpledateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            viewpageadapterdate.setText(simpledateformat.format(date));
        }
        catch (ParseException parseexception)
        {
            parseexception.printStackTrace();
        }
        dialogimagecredit.setText(Html.fromHtml((new StringBuilder()).append("<b>Image Courtesy- </b>").append( (credits)).toString()));

        if (youtubelink.equals("")){
            dialogviewwatchvideolayout.setVisibility(View.GONE);
        }

        imgDisplay.setScaleType(ImageView.ScaleType.FIT_XY);

        Display display = _activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated

        imgDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1)
            {
                if (dialogviewbuttonlayout.getVisibility() == View.VISIBLE) {
                    dialogviewbuttonlayout.setVisibility(View.GONE);
                    dialogimagecreditlayout.setVisibility(View.GONE);
                    Animation anim = AnimationUtils.loadAnimation(
                            _activity, R.anim.slide_out_to_bottom
                    );
                    anim.setDuration(500);

                    dialogviewbuttonlayout.setAnimation(anim);
                } else {
                    Animation anim = AnimationUtils.loadAnimation(
                            _activity, R.anim.slide_in_from_bottom
                    );
                    anim.setDuration(500);

                    dialogviewbuttonlayout.setAnimation(anim);
                    dialogviewbuttonlayout.setVisibility(View.VISIBLE);
                }
                //timelineguide.setVisibility(View.GONE);
            }
        });
        final  String finalYoutubelink = youtubelink;
        dialogviewwatchvideolayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                Intent k = new Intent(_activity,VideoPlayer.class);
                k.putExtra("watch", finalYoutubelink);
                k.putStringArrayListExtra("youtubeVideoId",new ArrayList<String>());
                _activity.startActivity(k);
            }
        });

        dialoginfolayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1)
            {
                dialogimagecreditlayout.setVisibility(View.VISIBLE);
            }

        });

        final  String finalmagename = imagename;
        dialogsharelayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1)
            {
                new ShareImageTask(_activity, finalmagename);
            }

        });
        File directory = null;
        final File file;
        MyDirectory myDirectory = new MyDirectory();
        directory = myDirectory.getDirectory();
        if (imagename.contains(".png"))
            file = new File(directory, imagename);
        else
            file = new File(directory, imagename+".png");
        if (file.exists()) {

            String imgPath = file.getAbsolutePath();
            Picasso.with(getcontext)
                    .load(file)
                    .resize(width, height)
                    .into(imgDisplay);
        }else {
            String imgageUrl;
            if (isS3){
                imgageUrl = "http://d2vwmcbs3lyudp.cloudfront.net/"+imagename;
                Log.e("imagname",imagename);
            }else {
                imgageUrl = getcontext.getResources().getString(R.string.url) + imagename + ".png";
            }

            Picasso.with(getcontext)
                    .load(imgageUrl)
                    .resize(width, height)
                    .into(imgDisplay);
            try {
                SavingCache savingCache = new SavingCache(getcontext);
                savingCache.open();
                savingCache.createEntry(1, imagename, timestamp);
                savingCache.close();
                Log.e("Saved Cache",imagename);
            }catch (Exception e){

            }
            //imgDisplay.setLayoutParams(layoutParams);
        }


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    public boolean isViewFromObject(View view, Object obj)
    {
        return view == (RelativeLayout)obj;
    }

    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

}
