// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package best.news.shorts.app.glance;

import android.content.Intent;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

// Referenced classes of package best.news.shorts.app.glance:
//            Controller

public class VideoPlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private String watchthis;
    private ArrayList<String> youtubeVideoId;
    private YouTubePlayer player;
    private int[] current={0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent i = getIntent();
        watchthis = i.getStringExtra("watch");
        youtubeVideoId = i.getStringArrayListExtra("youtubeVideoId");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
    }


    public void onInitializationSuccess(YouTubePlayer.Provider provider,final YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            Controller.getInstance().trackEvent("Video", watchthis, "user");

            playVideo(player);

            /*
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (player.isPlaying()) {
                            handler.postDelayed(this, 1000L);
                        } else {
                            handler.removeCallbacks(this);
                            watchthis = youtubeVideoId.get(current[0]++);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    youTubeView.initialize(Config.YOUTUBE_API_KEY, VideoPlayer.this);
                                }
                            });
                        }
                    }catch (Exception e){

                    }
                }
            }, 1000L);
            */
        }

    }

    private void playVideo(final YouTubePlayer player) {
        this.player =player;
        player.setShowFullscreenButton(false);

        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        Log.e("ist Size ", ""+youtubeVideoId.size());

        for (int x = 0; x<youtubeVideoId.size();x++){
            if (youtubeVideoId.get(x).equals(watchthis)){
                Log.e("List Size "+x, ""+youtubeVideoId.get(x));
                current[0] = x+1;
                break;
            }else {
                Log.e("List Size "+x, ""+youtubeVideoId.get(x));
                current[0]++;
            }
        }
        Log.e("At", ""+current[0]);
        final String[] sp = watchthis.split("\\?");
        Log.e("Length", String.valueOf(sp.length));


        if (sp.length > 1) {
            Log.e("Palyer1", sp[1]);
            player.loadVideo(sp[0], Integer.parseInt(sp[1]));


        } else {
            Log.e("Palyer2", sp[0]);
            player.loadVideo(sp[0]);
        }


        int duration = -1;
        if (sp.length > 2) {
            duration =Integer.parseInt(sp[2]);
        }else {
            while (duration<0)
            duration =player.getDurationMillis();
        }
        Log.e("duration :" ,"" +duration);
        final Handler handler = new Handler();
        final int finalDuration = duration;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if ((player.getCurrentTimeMillis() < finalDuration)) {
                        Log.e("Playing :" ,"Total= " + finalDuration
                                + " Is = " + player.getCurrentTimeMillis());
                        handler.postDelayed(this, 500L);
                    } else {

                        if (current[0]<youtubeVideoId.size()){
                            Log.e("Playing :" ,"Next");
                            watchthis = youtubeVideoId.get(current[0]++);
                            playVideo(player);
                                /*
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        youTubeView.initialize(Config.YOUTUBE_API_KEY, VideoPlayer.this);
                                    }
                                });*/
                            //handler.postDelayed(this, 1000L);
                        }else {
                            Log.e("Finishing :" ,"Man");
                            finish();
                        }
                    }
                }catch (Exception e){
                    Log.e("Ex Player" , e.toString());
                }
            }
        }, 1000L);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = getString(R.string.player_error)+ errorReason.toString();
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
// Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
            if (player.isPlaying())
                player.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}