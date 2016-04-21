// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.view.View;

public class ZoomOutPageTransformer
    implements android.support.v4.view.ViewPager.PageTransformer
{

    private final float scalingStart;

    public ZoomOutPageTransformer(float f)
    {
        scalingStart = 1.0F - f;
    }

    public void transformPage(View view, float f)
    {
        if (f >= 0.0F)
        {
            int i = view.getWidth();
            float f1 = scalingStart;
            view.setAlpha(1.0F - f);
            view.setTranslationX((float)i * (1.0F - f) - (float)i);
        }
    }
}
