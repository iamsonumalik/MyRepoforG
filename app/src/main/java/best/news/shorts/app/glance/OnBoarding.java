// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

// Referenced classes of package best.news.shorts.app.glance:
//            OnBoardingAdapter, AllCategory

public class OnBoarding extends Activity
{

    private int currentPosition;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private ImageView image6;
    private ImageView image7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.boardingPager);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        image7 = (ImageView) findViewById(R.id.image7);
        image4.setVisibility(View.GONE);
        image5.setVisibility(View.GONE);
        image6.setVisibility(View.GONE);
        image7.setVisibility(View.GONE);


        final ArrayList<String> image = new ArrayList<>();
        image.add("1");
        image.add("2");
        image.add("3");
        /*image.add("4");
        image.add("5");
        image.add("6");
        image.add("7");
        image.add("7");*/
        OnBoardingAdapter onBoardingAdapter = new OnBoardingAdapter(OnBoarding.this,image);
        viewPager.setAdapter(onBoardingAdapter);
        viewPager.setCurrentItem(0);
        setDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (position==2){
                    Intent i = new Intent(OnBoarding.this, AllCategory.class);
                    i.putExtra("moveto", "");
                    i.putExtra("isnews", true);
                    i.putExtra("fromnoti", false);
                    startActivity(i);
                    finish();
                }else {
                    setDots(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setDots(int dots) {
        image1.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        image2.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        image3.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        image4.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        image5.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        image6.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        image7.setImageDrawable(getResources().getDrawable(R.drawable.graydot));
        switch (dots){
            case 0:
                image1.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
            case 1:
                image2.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
            case 2:
                image3.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
            case 3:
                image4.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
            case 4:
                image5.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
            case 5:
                image6.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
            case 6:
                image7.setImageDrawable(getResources().getDrawable(R.drawable.whitedot));
                break;
        }
    }
}