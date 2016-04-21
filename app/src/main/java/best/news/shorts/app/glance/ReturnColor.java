// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;


public class ReturnColor {
    public int colorint(int x){
        if (x==0){
            return R.color.First;
        }else if(x==1){
            return R.color.second;
        }else if (x==2){
            return R.color.three;
        }else if (x==3){
            return R.color.four;
        } else if (x==4){
            return R.color.five;
        } else if (x==5){
            return R.color.six;
        } else if (x==6){
            return R.color.seven;
        } else if (x==7){
            return R.color.eight;
        }else {
            return R.color.last;
        }
    }
}