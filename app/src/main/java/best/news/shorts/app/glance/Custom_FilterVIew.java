package best.news.shorts.app.glance;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by sonu on 22/4/16.
 */
public class Custom_FilterVIew extends ArrayAdapter
{


    private final Resources getres;
    private final SharedPreferences prefs;
    private static final String MY_PREFS_NAME = "MySetting";
    private final SharedPreferences.Editor editor;
    private Activity _activity;
    private final Context context;
    private final String[] feedarray;
    private TextView filternametv;
    private TextView filterfollowtv;


    public Custom_FilterVIew(Context context1,
                             String[] feedarray,
                             Resources resources,
                             Activity activity, SharedPreferences prefs)
    {
        super(context1, R.layout.timeline_layout, feedarray);
        context = context1;
        getres = resources;
        _activity = activity;
        this.feedarray = feedarray;
        this.prefs = prefs;
        editor = activity.getSharedPreferences(MY_PREFS_NAME, 0).edit();
    }

    public View getView(final int view_pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.filtetagslistviewlayout, parent, false);

        filternametv = (TextView) rowView.findViewById(R.id.filternametv);
        filterfollowtv = (TextView) rowView.findViewById(R.id.filterfollowtv);

        Typeface lodingfont = Typeface.createFromAsset(getres.getAssets(), "lodingfont.ttf");
        filternametv.setTypeface(lodingfont);
        filterfollowtv.setTypeface(lodingfont);
        String item_feed = feedarray[view_pos];

        filternametv.setText(item_feed);

        if (prefs.getString("myfeed","").contains(item_feed)){
            filterfollowtv.setText("Unfollow");
        }else {
            filterfollowtv.setText("Follow");
        }

        final TextView finalfilterfollowtv =filterfollowtv;
        final String finalitem_feed = item_feed;
        finalfilterfollowtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myfeed = prefs.getString("myfeed","");
                if (prefs.getString("myfeed","").contains(finalitem_feed)){
                    myfeed = myfeed.replaceAll(finalitem_feed+",","");
                    editor.putString("myfeed", myfeed);
                    editor.commit();
                    editor.apply();
                    finalfilterfollowtv.setText("Follow");
                }else {
                    myfeed = myfeed + finalitem_feed + ",";
                    editor.putString("myfeed", myfeed);
                    editor.commit();
                    editor.apply();
                    finalfilterfollowtv.setText("Unfollow");
                }
            }
        });
        return rowView;
    }



}
