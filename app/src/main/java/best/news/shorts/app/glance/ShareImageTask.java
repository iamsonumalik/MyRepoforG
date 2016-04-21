package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;

// Referenced classes of package best.news.shorts.app.glance:
//            MyDirectory, Controller

public class ShareImageTask
{

    private  File file;
    private MyDirectory myDirectory = null;
    String phone;
    Activity get;
    private String quote="Download Glance. Get Crisp News with Videos and Timeline to know history behind every story.";

    public ShareImageTask(Activity getactivit, String item_time)
    {
        this.phone = item_time;
        this.get  = getactivit;
        Log.e("Share","ShareKr");


        try {
            myDirectory = new MyDirectory();
            File SDCardRoot = myDirectory.getDirectory();
            String filename;
            if (phone.contains(".png")){
                filename=phone;
            }else
                filename=phone+".png";
            Log.i("Local filename:",""+filename);
            file = new File(SDCardRoot,filename);
            if (!file.exists()){
                downloadnow();
            }else {
                Uri bmpUri = Uri.fromFile(file);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, quote + "\nmarket://details?id=best.news.shorts.app.glance");
                    shareIntent.setType("*/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // Launch sharing dialog for image
                    get.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    Controller.getInstance().trackEvent("Share", phone, "user");
                } else {
                    Log.e("EC", "Empf");
                    // ...sharing failed, handle error
                }
            }
        } catch (Exception e) {
            Log.e("Share1", e.toString());
        }

    }
    private void downloadnow() {

        String url;
        if (!(phone.contains(".png")))
            url = get.getResources().getString(R.string.url) + phone + ".png";
        else
            url="http://d2vwmcbs3lyudp.cloudfront.net/"+phone;
        Picasso.with(get)
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        try {

                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            Uri bmpUri = Uri.fromFile(file);
                            if (bmpUri != null) {
                                // Construct a ShareIntent with link to image
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, quote +"\nmarket://details?id=best.news.shorts.app.glance");
                                shareIntent.setType("*/*");
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                // Launch sharing dialog for image
                                get.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                                Controller.getInstance().trackEvent("Share", phone, "user");
                            } else {
                                Log.e("EC", "Empf");
                                // ...sharing failed, handle error
                            }

                            out.close();
                        }catch (Exception e){
                            Log.e("Share2", e.toString());
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                });
    }

}