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

public class SavingPublicId
{
    private static class SHHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
            sqlitedatabase.execSQL("CREATE TABLE " +
                    "publicid_table (" +
                    "sno INTEGER ," +
                    "publicid VARCHAR2(400), " +
                    "headline VARCHAR2(400), " +
                    "id VARCHAR2(400) PRIMARY KEY , " +
                    "timestampcreated VARCHAR2(400), " +
                    "category VARCHAR2(40), " +
                    "issimplified VARCHAR2(10), " +
                    "isviral VARCHAR2(10), " +
                    "editorRating INTEGER, " +
                    "state VARCHAR2(20), " +
                    "breakingNews VARCHAR2(10), " +
                    "enabled VARCHAR2(10), " +
                    "othertags VARCHAR2(400), " +
                    "creditname VARCHAR2(400), " +
                    "isFact VARCHAR2(10), " +
                    "isS VARCHAR2(10), " +
                    "linkedToNews VARCHAR2(400));");
        }

        public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
        {
            sqlitedatabase.execSQL("Drop table if exists publicid_table");
            onCreate(sqlitedatabase);
        }

        public SHHelper(Context context)
        {
            super(context, "bestnewsshortsapppublicid", null, 1);
        }
    }


    public static final String DATABASE_NAME = "bestnewsshortsapppublicid";
    public static final String DATABASE_TABLE1 = "publicid_table";
    public static final int DATABASE_VERSION = 1;
    public static final String History__id = "id";
    public static final String History_breakingNews = "breakingNews";
    public static final String History_category = "category";
    public static final String History_creditname = "creditname";
    public static final String History_editorsrating = "editorRating";
    public static final String History_enabled = "enabled";
    public static final String History_headline = "headline";
    public static final String History_isFact = "isFact";
    public static final String History_isS3 = "isS";
    public static final String History_issimplified = "issimplified";
    public static final String History_isviral = "isviral";
    public static final String History_linkedToNews = "linkedToNews";
    public static final String History_othertags = "othertags";
    public static final String History_public_id = "publicid";
    public static final String History_sno = "sno";
    public static final String History_state = "state";
    public static final String History_timestampcreated = "timestampcreated";
    private static SQLiteDatabase ourdatabase;
    private SHHelper ourHelper;
    private final Context ourcontext;

    public SavingPublicId(Context context)
    {
        ourcontext = context;
    }

    public void close()
        throws Exception
    {
        ourHelper.close();
    }

    public long createEntry(int i, String s, String s1, String s2, String s3, String s4, String s5, 
            int j, String s6, String s7, String s8, String s9, String s10, String s11, 
            String s12, String s13, String s14)
        throws Exception
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("sno", Integer.valueOf(i));
        contentvalues.put("publicid", s1);
        contentvalues.put("id", s);
        contentvalues.put("timestampcreated", s2);
        contentvalues.put("category", s3);
        contentvalues.put("issimplified", s4);
        contentvalues.put("isviral", s5);
        contentvalues.put("editorRating", Integer.valueOf(j));
        contentvalues.put("state", s6);
        contentvalues.put("breakingNews", s7);
        contentvalues.put("enabled", s8);
        contentvalues.put("othertags", s9);
        contentvalues.put("linkedToNews", s10);
        contentvalues.put("isFact", s11);
        contentvalues.put("headline", s12);
        contentvalues.put("isS", s13);
        contentvalues.put("creditname", s14);
        return ourdatabase.insert("publicid_table", null, contentvalues);
    }

    public void deletePost(String s)
    {
        ourdatabase.delete("publicid_table", (new StringBuilder()).append("id='").append(s).append("'").toString(), null);
    }

    public void dropdb()
    {
        ourcontext.deleteDatabase("bestnewsshortsapppublicid");
    }

    public ArrayList getAll()
    {
        Cursor cursor = ourdatabase.query("publicid_table", new String[] {
            "id", "publicid", "isS", "category", "isviral", "headline", "othertags", "linkedToNews", "timestampcreated"
        }, "isviral='false'", null, null, null, "timestampcreated ASC");
        ArrayList arraylist = new ArrayList();
        int j = cursor.getColumnIndex("id");
        int k = cursor.getColumnIndex("publicid");
        int l = cursor.getColumnIndex("othertags");
        int i1 = cursor.getColumnIndex("linkedToNews");
        int j1 = cursor.getColumnIndex("timestampcreated");
        int k1 = cursor.getColumnIndex("isS");
        int i = 0;
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst() && i < 200; cursor.moveToPrevious())
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

    public ArrayList getByCategory(String s)
    {
        Object obj = ourdatabase;
        s = (new StringBuilder()).append("category='").append(s).append("' AND ").append("isviral").append("='false'").toString();
        Cursor cursor = ourdatabase.query("publicid_table", new String[] {
            "id", "publicid", "isS", "category", "timestampcreated", "headline", "isviral", "othertags", "linkedToNews"
        }, s, null, null, null, "timestampcreated ASC");
        obj = new ArrayList();
        int j = cursor.getColumnIndex("id");
        int k = cursor.getColumnIndex("publicid");
        int l = cursor.getColumnIndex("othertags");
        int i1 = cursor.getColumnIndex("linkedToNews");
        int j1 = cursor.getColumnIndex("timestampcreated");
        int k1 = cursor.getColumnIndex("isS");
        int i = 0;
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst() && i < 200; cursor.moveToPrevious())
        {
            ((ArrayList) (obj)).add(cursor.getString(k));
            ((ArrayList) (obj)).add(cursor.getString(l));
            ((ArrayList) (obj)).add(cursor.getString(j));
            ((ArrayList) (obj)).add(cursor.getString(i1));
            ((ArrayList) (obj)).add(cursor.getString(j1));
            ((ArrayList) (obj)).add(cursor.getString(k1));
            i++;
        }

        return ((ArrayList) (obj));
    }

    public ArrayList getCategoryList()
    {
        Cursor cursor = ourdatabase.rawQuery("select DISTINCT category from publicid_table", null);
        int i = cursor.getColumnIndex("category");
        ArrayList arraylist = new ArrayList();
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst(); cursor.moveToPrevious())
        {
            arraylist.add(cursor.getString(i));
        }

        return arraylist;
    }

    public String getCredits(String s)
    {
        Object obj = ourdatabase;
        s = (new StringBuilder()).append("publicid='").append(s).append("'").toString();
        obj = ((SQLiteDatabase) (obj)).query("publicid_table", new String[] {
            "id", "publicid", "creditname", "timestampcreated"
        }, s, null, null, null, "timestampcreated ASC");
        s = "-1";
        int i = ((Cursor) (obj)).getColumnIndex("creditname");
        ((Cursor) (obj)).moveToLast();
        if (!((Cursor) (obj)).isBeforeFirst())
        {
            s = ((Cursor) (obj)).getString(i);
            Log.e("Result ", s);
        }
        return s;
    }

    public boolean getData()
    {
        boolean flag = false;
        Cursor cursor = ourdatabase.query("publicid_table", new String[] {
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

    public String getLatetsTime()
    {
        Cursor cursor = ourdatabase.query("publicid_table", new String[] {
            "id", "publicid", "isviral", "timestampcreated"
        }, null, null, null, null, "timestampcreated ASC");
        String s = "-1";
        int i = cursor.getColumnIndex("timestampcreated");
        cursor.moveToLast();
        if (!cursor.isBeforeFirst())
        {
            s = cursor.getString(i);
        }
        return s;
    }

    public ArrayList getSimplified(String s)
    {
        Object obj = ourdatabase;
        s = (new StringBuilder()).append("issimplified='").append(s).append("' AND ").append("isviral").append("='false'").toString();
        Cursor cursor = ourdatabase.query("publicid_table", new String[] {
            "id", "publicid", "isS", "category", "issimplified", "headline", "timestampcreated", "isviral", "othertags", "linkedToNews"
        }, s, null, null, null, "timestampcreated ASC");
        obj = new ArrayList();
        int j = cursor.getColumnIndex("id");
        int k = cursor.getColumnIndex("publicid");
        int l = cursor.getColumnIndex("othertags");
        int i1 = cursor.getColumnIndex("linkedToNews");
        int j1 = cursor.getColumnIndex("timestampcreated");
        int k1 = cursor.getColumnIndex("isS");
        int i = 0;
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst() && i < 200; cursor.moveToPrevious())
        {
            ((ArrayList) (obj)).add(cursor.getString(k));
            ((ArrayList) (obj)).add(cursor.getString(l));
            ((ArrayList) (obj)).add(cursor.getString(j));
            ((ArrayList) (obj)).add(cursor.getString(i1));
            ((ArrayList) (obj)).add(cursor.getString(j1));
            ((ArrayList) (obj)).add(cursor.getString(k1));
            i++;
        }

        return ((ArrayList) (obj));
    }

    public int getTotal()
    {
        return ourdatabase.query("publicid_table", new String[] {
            "publicid"
        }, null, null, null, null, "timestampcreated DESC").getCount();
    }

    public String get_id(String s)
    {
        String s1 = "";
        Object obj = ourdatabase;
        s = (new StringBuilder()).append("publicid='").append(s).append("'").toString();
        obj = ((SQLiteDatabase) (obj)).query("publicid_table", new String[] {
            "id", "publicid"
        }, s, null, null, null, null);
        int i = ((Cursor) (obj)).getColumnIndex("id");
        ((Cursor) (obj)).moveToLast();
        s = s1;
        for (; !((Cursor) (obj)).isBeforeFirst(); ((Cursor) (obj)).moveToPrevious())
        {
            s = ((Cursor) (obj)).getString(i);
        }

        return s;
    }

    public String getp_id(String s)
    {
        String s1 = "";
        Object obj = ourdatabase;
        s = (new StringBuilder()).append("id='").append(s).append("'").toString();
        obj = ((SQLiteDatabase) (obj)).query("publicid_table", new String[] {
            "id", "publicid"
        }, s, null, null, null, null);
        int i = ((Cursor) (obj)).getColumnIndex("publicid");
        ((Cursor) (obj)).moveToLast();
        s = s1;
        for (; !((Cursor) (obj)).isBeforeFirst(); ((Cursor) (obj)).moveToPrevious())
        {
            s = ((Cursor) (obj)).getString(i);
        }

        return s;
    }

    public ArrayList getviral()
    {
        Cursor cursor = ourdatabase.query("publicid_table", new String[] {
            "id", "publicid", "isS", "category", "issimplified", "headline", "timestampcreated", "isviral", "othertags", "linkedToNews"
        }, "isviral='true'", null, null, null, "timestampcreated ASC");
        ArrayList arraylist = new ArrayList();
        int j = cursor.getColumnIndex("id");
        int k = cursor.getColumnIndex("publicid");
        int l = cursor.getColumnIndex("othertags");
        int i1 = cursor.getColumnIndex("linkedToNews");
        int j1 = cursor.getColumnIndex("timestampcreated");
        int k1 = cursor.getColumnIndex("isS");
        int i = 0;
        cursor.moveToLast();
        for (; !cursor.isBeforeFirst() && i < 200; cursor.moveToPrevious())
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

    public SavingPublicId open()
        throws Exception
    {
        ourHelper = new SHHelper(ourcontext);
        ourdatabase = ourHelper.getWritableDatabase();
        return this;
    }
}
