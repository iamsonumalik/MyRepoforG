// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class SavingViral
{
    private static class SHHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            sqlitedatabase.execSQL("CREATE TABLE "+DATABASE_TABLE1+" (" +
                    ""+History_public_id+" VARCHAR2(400), " +
                    ""+History_headline+" VARCHAR2(400), " +
                    ""+History__id+" VARCHAR2(400) PRIMARY KEY , " +
                    ""+History_timestampcreated+" VARCHAR2(400), " +
                    ""+History_viraltags+" VARCHAR2(400), " +
                    ""+History_youtubeVideoId+" VARCHAR2(400), " +
                    ""+History_youtubeVideoDuration+" VARCHAR2(40)); ");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
            sqlitedatabase.execSQL("Drop table if exists viral_table");
            onCreate(sqlitedatabase);
        }

        public SHHelper(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }
    }


    public static final String DATABASE_NAME = "bestnewsshortsappviral";
    public static final String DATABASE_TABLE1 = "viral_table";
    public static final int DATABASE_VERSION = 1;
    public static final String History__id = "viralid";
    public static final String History_headline = "viralheadline";
    public static final String History_youtubeVideoId = "youtubeVideoId";
    public static final String History_public_id = "viralpublicid";
    public static final String History_viraltags = "viraltags";
    public static final String History_youtubeVideoDuration = "youtubeVideoDuration";
    public static final String History_timestampcreated = "timestampcreated";
    private static SQLiteDatabase ourdatabase;
    private SHHelper ourHelper;
    private final Context ourcontext;

    public SavingViral(Context context)
    {
        ourcontext = context;
    }

    public void close()
        throws Exception {
        ourHelper.close();
    }

    public long createEntry(String item_headline
            , String item_public_id
            , String item_duration
            , String item_tags
            , String item__id
            , String item_viraltimestampcreated
            , String item_youtubeVideoId)
        throws Exception
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(History_headline, item_headline);
        contentvalues.put(History_public_id, item_public_id);
        contentvalues.put(History_youtubeVideoDuration, item_duration);
        contentvalues.put(History_viraltags, item_tags);
        contentvalues.put(History__id, item__id);
        contentvalues.put(History_timestampcreated, item_viraltimestampcreated);
        contentvalues.put(History_youtubeVideoId, item_youtubeVideoId);
        return ourdatabase.insert(DATABASE_TABLE1, null, contentvalues);
    }

    public void deletePost(String s)
    {
        ourdatabase.delete("viral_table", (new StringBuilder()).append(""+History__id+"='").append(s).append("'").toString(), null);
    }

    public void dropdb()
    {
        ourcontext.deleteDatabase("bestnewsshortsappviral");
    }

    public ArrayList<String> getAll()
    {
        Cursor cursor = ourdatabase.query("viral_table", new String[] {
                History__id,
                History_headline,
                History_youtubeVideoId,
                History_public_id,
                History_viraltags,
                History_youtubeVideoDuration,
                History_timestampcreated
        }, null, null, null, null, History_timestampcreated+" ASC");
        ArrayList<String> arraylist = new ArrayList<String>();


        int i_1 = cursor.getColumnIndex(History_headline);
        int i_2 = cursor.getColumnIndex(History__id);
        int i_3 = cursor.getColumnIndex(History_timestampcreated);
        int i_4 = cursor.getColumnIndex(History_youtubeVideoId);
        int i_5 = cursor.getColumnIndex(History_youtubeVideoDuration);
        int i_6 = cursor.getColumnIndex(History_public_id);
        int i_7 = cursor.getColumnIndex(History_viraltags);

        cursor.moveToLast();
        for (; !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            arraylist.add(cursor.getString(i_1));
            arraylist.add(cursor.getString(i_2));
            arraylist.add(cursor.getString(i_3));
            arraylist.add(cursor.getString(i_4));
            arraylist.add(cursor.getString(i_5));
            arraylist.add(cursor.getString(i_6));
            arraylist.add(cursor.getString(i_7));


        }

        return arraylist;
    }


    public boolean getData(String _id)
    {
        boolean flag = false;
        Cursor cursor = ourdatabase.query("viral_table", new String[] {
                History__id
        }, History__id+"='"+_id+"'", null, null, null, null);
        cursor.getColumnIndex("viralpublicid");
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            flag = true;
        }

        return flag;
    }


    public int getTotal()
    {
        return ourdatabase.query("viral_table", new String[] {
            "viralpublicid"
        }, null, null, null, null, "viraltimestampcreated DESC").getCount();
    }


    public SavingViral open()
        throws Exception {
        ourHelper = new SHHelper(ourcontext);
        ourdatabase = ourHelper.getWritableDatabase();
        return this;
    }


}
