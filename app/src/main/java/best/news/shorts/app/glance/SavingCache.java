// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SavingCache
{
    private static class SHHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            sqlitedatabase.execSQL("CREATE TABLE cache_table (sno INTEGER ,publicid VARCHAR2(400) PRIMARY KEY , timestampcreated VARCHAR2(400));");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
            sqlitedatabase.execSQL("Drop table if exists cache_table");
            onCreate(sqlitedatabase);
        }

        public SHHelper(Context context)
        {
            super(context, "bestnewsshortsappglanceruncache", null, 1);
        }
    }


    public static final String DATABASE_NAME = "bestnewsshortsappglanceruncache";
    public static final String DATABASE_TABLE1 = "cache_table";
    public static final int DATABASE_VERSION = 1;
    public static final String History_cache_publicid = "publicid";
    public static final String History_cache_timestamp = "timestampcreated";
    public static final String History_sno = "sno";
    private static SQLiteDatabase ourdatabase;
    private SHHelper ourHelper;
    private final Context ourcontext;

    public SavingCache(Context context)
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
        contentvalues.put("timestampcreated", s1);
        return ourdatabase.insert("cache_table", null, contentvalues);
    }

    public void deleteItem(String s)
    {
        ourdatabase.delete("cache_table", (new StringBuilder()).append("publicid='").append(s).append("'").toString(), null);
    }

    public void dropdb()
    {
        ourcontext.deleteDatabase("bestnewsshortsappglanceruncache");
    }

    public ArrayList getAll()
    {
        Cursor cursor = ourdatabase.query("cache_table", new String[] {
            "publicid", "timestampcreated"
        }, null, null, null, null, "timestampcreated DESC");
        ArrayList arraylist = new ArrayList();
        int i = cursor.getColumnIndex("publicid");
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            arraylist.add(cursor.getString(i));
        }

        return arraylist;
    }

    public int getTotal()
    {
        return ourdatabase.query("cache_table", new String[] {
            "publicid", "timestampcreated"
        }, null, null, null, null, "timestampcreated DESC").getCount();
    }

    public SavingCache open()
        throws Exception
    {
        ourHelper = new SHHelper(ourcontext);
        ourdatabase = ourHelper.getWritableDatabase();
        return this;
    }
}
