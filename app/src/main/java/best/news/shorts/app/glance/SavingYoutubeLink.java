// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SavingYoutubeLink
{
    private static class SHHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            sqlitedatabase.execSQL("CREATE TABLE youtubevideoid_table (sno INTEGER ,publicid VARCHAR2(400) PRIMARY KEY , youtubevideoid VARCHAR2(400));");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
            sqlitedatabase.execSQL("Drop table if exists youtubevideoid_table");
            onCreate(sqlitedatabase);
        }

        public SHHelper(Context context)
        {
            super(context, "bestnewsshortsappyoutubevideoid", null, 1);
        }
    }


    public static final String DATABASE_NAME = "bestnewsshortsappyoutubevideoid";
    public static final String DATABASE_TABLE1 = "youtubevideoid_table";
    public static final int DATABASE_VERSION = 1;
    public static final String History_public_id = "publicid";
    public static final String History_sno = "sno";
    public static final String History_youtubeVideoId = "youtubevideoid";
    private static SQLiteDatabase ourdatabase;
    private SHHelper ourHelper;
    private final Context ourcontext;

    public SavingYoutubeLink(Context context)
    {
        ourcontext = context;
    }

    public void close()
        throws Exception
    {
        ourHelper.close();
    }

    public long createEntry(int i, String s, String s1)
        throws Exception
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("sno", Integer.valueOf(i));
        contentvalues.put("publicid", s);
        contentvalues.put("youtubevideoid", s1);
        return ourdatabase.insert("youtubevideoid_table", null, contentvalues);
    }

    public void deleteLink(String s)
    {
        ourdatabase.delete("youtubevideoid_table", (new StringBuilder()).append("publicid='").append(s).append("'").toString(), null);
    }

    public void dropdb()
    {
        ourcontext.deleteDatabase("bestnewsshortsappyoutubevideoid");
    }

    public boolean getData()
    {
        boolean flag = false;
        Cursor cursor = ourdatabase.query("youtubevideoid_table", new String[] {
            "sno"
        }, null, null, null, null, null);
        cursor.getColumnIndex("sno");
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            flag = true;
        }

        return flag;
    }

    public String get_youtubeVideoId(String s)
    {
        String s1 = "";
        Object obj = ourdatabase;
        s = (new StringBuilder()).append("publicid='").append(s).append("'").toString();
        obj = ((SQLiteDatabase) (obj)).query("youtubevideoid_table", new String[] {
            "publicid", "youtubevideoid"
        }, s, null, null, null, null);
        int i = ((Cursor) (obj)).getColumnIndex("youtubevideoid");
        ((Cursor) (obj)).moveToLast();
        s = s1;
        for (; !((Cursor) (obj)).isBeforeFirst(); ((Cursor) (obj)).moveToPrevious())
        {
            s = ((Cursor) (obj)).getString(i);
        }

        return s;
    }

    public SavingYoutubeLink open()
        throws Exception
    {
        ourHelper = new SHHelper(ourcontext);
        ourdatabase = ourHelper.getWritableDatabase();
        return this;
    }
}
