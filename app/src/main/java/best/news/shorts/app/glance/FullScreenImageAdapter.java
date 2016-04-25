// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.File;
import java.util.ArrayList;

// Referenced classes of package best.news.shorts.app.glance:
//            MyDirectory, SavingCache, SavingYoutubeLink, VideoPlayer

public class FullScreenImageAdapter extends PagerAdapter
{

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private android.graphics.BitmapFactory.Options bmOptions;
    private final RelativeLayout bookmarklayout;
    public ImageView currentImage;
    private final Context getcontext;
    RelativeLayout getmenu;
    Resources getres;
    private LinearLayout imagecreditlayout;
    private LayoutInflater inflater;
    private final ArrayList<String> isS3lis;
    private final boolean isviral;
    private final ArrayList<String> timestamplist;

    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> arraylist,
                                  Resources resources,
                                  Context context,
                                  RelativeLayout relativelayout,
                                  boolean flag,
                                  ArrayList arraylist1,
                                  LinearLayout linearlayout,
                                  ArrayList<String> arraylist2, RelativeLayout relativelayout1)
    {
        _activity = activity;
        _imagePaths = arraylist;
        timestamplist = arraylist1;
        getres = resources;
        getcontext = context;
        currentImage = new ImageView(_activity);
        getmenu = relativelayout;
        isviral = flag;
        bmOptions = new android.graphics.BitmapFactory.Options();
        imagecreditlayout = linearlayout;
        isS3lis = arraylist2;
        bookmarklayout = relativelayout1;
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
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        Display display = _activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        imgDisplay.setLayoutParams(layoutParams);
        Button playit = (Button) viewLayout.findViewById(R.id.playit);

        final String imagename = _imagePaths.get(position);
        final String dateget = timestamplist.get(position);
        final String isS3 = isS3lis.get(position);
        if (isviral){
            playit.setVisibility(View.VISIBLE);
        }else {
            playit.setVisibility(View.GONE);
        }
        imgDisplay.setScaleType(ImageView.ScaleType.FIT_XY);
        imgDisplay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1)
            {
                if (getmenu.getVisibility() == View.VISIBLE)
                {
                    imagecreditlayout.setVisibility(View.GONE);
                    Animation animation = AnimationUtils.loadAnimation(_activity, R.anim.slide_out_to_bottom);
                    Animation animation1 = AnimationUtils.loadAnimation(_activity, R.anim.slide_out_up);
                    getmenu.setAnimation(animation);
                    bookmarklayout.setAnimation(animation1);
                    getmenu.setVisibility(View.GONE);
                    bookmarklayout.setVisibility(View.GONE);
                    return;
                } else
                {
                    Animation animation = AnimationUtils.loadAnimation(_activity, R.anim.slide_in_from_bottom);
                    Animation animation1 = AnimationUtils.loadAnimation(_activity, R.anim.slide_in_up);
                    getmenu.setAnimation(animation);
                    bookmarklayout.setAnimation(animation1);
                    getmenu.setVisibility(View.VISIBLE);
                    bookmarklayout.setVisibility(View.VISIBLE);
                    return;
                }
            }

        });
        playit.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1)
            {
                String youtubelink = "";
                try{
                    SavingYoutubeLink savingYoutubeLink = new SavingYoutubeLink(_activity);
                    savingYoutubeLink.open();
                    youtubelink= savingYoutubeLink.get_youtubeVideoId(imagename);
                    savingYoutubeLink.close();
                }catch (Exception e){

                }
                Intent k = new Intent(_activity,VideoPlayer.class);
                k.putExtra("watch", youtubelink);
                k.putStringArrayListExtra("youtubeVideoId",new ArrayList<String>());
                _activity.startActivity(k);
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
            Log.e("isS3",isS3);
            if (isS3.equals("true")){
                imgageUrl = "http://d2vwmcbs3lyudp.cloudfront.net/"+imagename;
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
                savingCache.createEntry(1, imagename, dateget);
                savingCache.close();
                Log.e("Saved Cache",imagename);
            }catch (Exception e){

            }
            //imgDisplay.setLayoutParams(layoutParams);
        }


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



}
