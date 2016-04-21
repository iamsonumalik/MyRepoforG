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

public class SavingBookmark
{
    private static class SHHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            sqlitedatabase.execSQL("CREATE TABLE timestampcreated_table (sno INTEGER ,bookmark_id VARCHAR2(400) PRIMARY KEY , bookmark_pid VARCHAR2(400), bookmark_iss3 VARCHAR2(40), bookmark_linkktonews VARCHAR2(400), bookmark_othertags VARCHAR2(400), bookmark_timestampcreated VARCHAR2(400));");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
            sqlitedatabase.execSQL("Drop table if exists timestampcreated_table");
            onCreate(sqlitedatabase);
        }

        public SHHelper(Context context)
        {
            super(context, "bestnewsshortsapptimestampcreated", null, 1);
        }
    }


    public static final String DATABASE_NAME = "bestnewsshortsapptimestampcreated";
    public static final String DATABASE_TABLE1 = "timestampcreated_table";
    public static final int DATABASE_VERSION = 1;
    public static final String History__id = "bookmark_id";
    public static final String History_iss3 = "bookmark_iss3";
    public static final String History_linkktonews = "bookmark_linkktonews";
    public static final String History_othertags = "bookmark_othertags";
    public static final String History_public_id = "bookmark_pid";
    public static final String History_sno = "sno";
    public static final String History_timestampcreated = "bookmark_timestampcreated";
    private static SQLiteDatabase ourdatabase;
    private SHHelper ourHelper;
    private final Context ourcontext;

    public SavingBookmark(Context context)
    {
        ourcontext = context;
    }

    public boolean checkID(String s)
    {
        SQLiteDatabase sqlitedatabase = ourdatabase;
        s = (new StringBuilder()).append("bookmark_id='").append(s).append("'").toString();
        Cursor cursor = ourdatabase.query("timestampcreated_table", new String[] {
            "bookmark_id", "bookmark_pid", "bookmark_timestampcreated"
        }, s, null, null, null, "bookmark_timestampcreated ASC");
        boolean flag = false;
        cursor.moveToLast();
        if (!cursor.isBeforeFirst())
        {
            flag = true;
        }
        return flag;
    }

    public void close()
        throws Exception
    {
        ourHelper.close();
    }

    public long createEntry(int i, String s, String s1, String s2, String s3, String s4, String s5)
        throws Exception
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("sno", Integer.valueOf(i));
        contentvalues.put("bookmark_id", s);
        contentvalues.put("bookmark_pid", s1);
        contentvalues.put("bookmark_timestampcreated", s2);
        contentvalues.put("bookmark_othertags", s3);
        contentvalues.put("bookmark_linkktonews", s4);
        contentvalues.put("bookmark_iss3", s5);
        return ourdatabase.insert("timestampcreated_table", null, contentvalues);
    }

    public void deleteLink(String s)
    {
        ourdatabase.delete("timestampcreated_table", (new StringBuilder()).append("bookmark_id='").append(s).append("'").toString(), null);
    }

    public void dropdb()
    {
        ourcontext.deleteDatabase("bestnewsshortsapptimestampcreated");
    }

    public ArrayList getAll()
    {
        Cursor cursor = ourdatabase.query("timestampcreated_table", new String[] {
            "bookmark_id", "bookmark_pid", "bookmark_linkktonews", "bookmark_iss3", "bookmark_othertags", "bookmark_timestampcreated"
        }, null, null, null, null, "bookmark_timestampcreated ASC");
        ArrayList arraylist = new ArrayList();
        int j = cursor.getColumnIndex("bookmark_id");
        int k = cursor.getColumnIndex("bookmark_pid");
        int l = cursor.getColumnIndex("bookmark_othertags");
        int i1 = cursor.getColumnIndex("bookmark_linkktonews");
        int j1 = cursor.getColumnIndex("bookmark_timestampcreated");
        int k1 = cursor.getColumnIndex("bookmark_iss3");
        int i = 0;
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            arraylist.add(cursor.getString(k));
            arraylist.add(cursor.getString(l));
            arraylist.add(cursor.getString(j));
            arraylist.add(cursor.getString(i1));
            arraylist.add(cursor.getString(j1));
            arraylist.add(cursor.getString(k1));
            i++;
        }

        return arraylist;
    }

    public boolean getData()
    {
        boolean flag = false;
        Cursor cursor = ourdatabase.query("timestampcreated_table", new String[] {
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

    public int getTotal()
    {
        return ourdatabase.query("timestampcreated_table", new String[] {
            "bookmark_pid", "bookmark_timestampcreated"
        }, null, null, null, null, "bookmark_timestampcreated DESC").getCount();
    }

    public SavingBookmark open()
        throws Exception
    {
        ourHelper = new SHHelper(ourcontext);
        ourdatabase = ourHelper.getWritableDatabase();
        return this;
    }
}
