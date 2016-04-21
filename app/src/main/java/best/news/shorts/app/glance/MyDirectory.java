// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.os.Environment;
import android.util.Log;
import java.io.File;
/**
 * Created by malik on 24/2/16.
 */
public class MyDirectory {
    public File getDirectory(){
        File directory = null;
        if (Environment.getExternalStorageState() == null) {
//create new file directory object
            directory = new File(Environment.getDataDirectory()
                    + "/Android/data/best.news.shorts.app.glance/cache");
/*
* this checks to see if there are any previous test photo files
* if there are any photos, they are deleted for the sake of
* memory
*/
// if no directory exists, create new directory
            if (!directory.exists()) {
                try {
                    directory.mkdirs();
                }catch (Exception e){
                    Log.e("Make Dir",e.toString());
                }
            }
// if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
// search for directory on SD card
            directory = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/best.news.shorts.app.glance/cache");
// if no directory exists, create new directory to store test
// results
            if (!directory.exists()) {
                try {
                    directory.mkdirs();
                }catch (Exception e){
                    Log.e("Make Dir",e.toString());
                }
            }
        }// end of SD card checking
        return directory;
    }
}