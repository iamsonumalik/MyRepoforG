// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
// Referenced classes of package best.news.shorts.app.glance:
//            AllCategory

public class OnBoardingAdapter extends PagerAdapter
{

    ArrayList<String> size;
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    // constructor
    public OnBoardingAdapter(Activity activity,
                             ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }
    @Override
    public int getCount() {
        return this._imagePaths.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imgDisplay;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.boarding_adapter, container,
                false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.boardingPagerImageView);
        setImageDisplay(imgDisplay,position);
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }
    private void setImageDisplay(ImageView imgDisplay, int position) {
        switch (position){
            case 0:
                imgDisplay.setImageDrawable(_activity.getResources().getDrawable(R.drawable.boardingc));
                break;
            case 1:
                imgDisplay.setImageDrawable(_activity.getResources().getDrawable(R.drawable.boardingf));
                break;
            case 2:
                imgDisplay.setImageDrawable(_activity.getResources().getDrawable(R.drawable.boardingg));
                imgDisplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(_activity, AllCategory.class);
                        i.putExtra("moveto", "");
                        i.putExtra("isnews", true);
                        i.putExtra("fromnoti", false);
                        _activity.startActivity(i);
                        _activity.finish();
                    }
                });
                break;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}