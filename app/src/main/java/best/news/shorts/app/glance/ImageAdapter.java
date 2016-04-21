package best.news.shorts.app.glance;

/**
 * Created by malik on 12/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import best.news.shorts.app.glance.R;

public class ImageAdapter extends BaseAdapter {
    private final ArrayList<Drawable> mimage_clips;
    private final Activity mact;
    private Context mContext;

    // Constructor
    public ImageAdapter(Activity loadingActivity, Context c, ArrayList<Drawable> image_clips) {
        mContext = c;
        mimage_clips = image_clips;
        mact = loadingActivity;
    }

    public int getCount() {
        return mimage_clips.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ImageView imageView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.loading_grid, parent, false);

            imageView = (ImageView) rowView.findViewById(R.id.loadinggridimageView);
            Display display = mact.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();  // deprecated
            imageView.setLayoutParams(new GridView.LayoutParams((width / 6)-5, (height / 9)));

        }
        else
        {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageBitmap(mimage_clips.get(position));
        imageView.setBackground(mimage_clips.get(position));
        return imageView;
    }


}