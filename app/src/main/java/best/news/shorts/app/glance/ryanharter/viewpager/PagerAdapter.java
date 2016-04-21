// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance.ryanharter.viewpager;


public abstract class PagerAdapter extends android.support.v4.view.PagerAdapter {
    /**
     * {@inheritDoc}
     * @deprecated Use {@link #getPageSize(int)} instead.
     */
    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }
    /**
     * Returns the proportional size (width or height depending on orientation)
     * of a given page as a percentage of the ViewPager's measured size from (0.f-1.f).
     *
     * @param position The position of the page requested
     * @return Proportional size for the given page position
     */
    public float getPageSize(int position) {
        return getPageWidth(position);
    }
}