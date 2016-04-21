// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import best.news.shorts.app.glance.gcm.GCMRegistrar;

// Referenced classes of package best.news.shorts.app.glance:
//            Controller

public class Storinggcm
{

    Controller aController;
    String accesstoken;
    Activity getact;
    AsyncTask mRegisterTask;

    public Storinggcm(Activity activity, String s)
    {
        accesstoken = s;
        getact = activity;
        aController = (Controller)activity.getApplicationContext();
        if (!aController.isConnectingToInternet())
        {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        GCMRegistrar.checkDevice(activity);
        GCMRegistrar.checkManifest(activity);
        String s1 = GCMRegistrar.getRegistrationId(activity);
        if (s1.equals(""))
        {
            GCMRegistrar.register(activity, new String[] {
                "978875855254"
            });
            return;
        } else
        {
            aController.register(activity, s, s1);
            return;
        }
    }
}
