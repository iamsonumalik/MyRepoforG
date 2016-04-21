// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;


public class Quotes {
    String []quoteslist;
    Quotes(){
        quoteslist = new String[]{
                "Hello,",
                "Hi,",
                ":-)",
                "What’s up..",
                "What’s new..",
                "Nice to see you.",
                "Howdy!",
                "Hiya!",
                "Namaskar,",
                "Well Hello!",
                "Hola!",
                "Salaam,"
        };
    }
    public String getQuoteslist(int pos) {
        return quoteslist[pos];
    }
}