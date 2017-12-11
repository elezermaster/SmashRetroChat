package io.emaster.smashretrochat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.model.ItemImage;


/**
 * Created by elezermaster on 15/08/16.
 */
// Custom pager adapter not using fragments
public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<ItemImage> mPages = new ArrayList<>();

    public CustomPagerAdapter(Context context, ArrayList<ItemImage> pages) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mPages = pages;
    }

    // Returns the number of pages to be displayed in the ViewPager.
    @Override
    public int getCount() {
        return mPages.size();
    }

    // Returns true if a particular object (page) is from a particular page
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // This method should create the page for the given position passed to it as an argument.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate the layout for the page
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        // Find and populate data into the page (i.e set the image)
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgViewPagerItem);
        // ...
        ItemImage itemImage = mPages.get(position);
        imageView.setImageBitmap(itemImage.getImage());
        // Add the page to the container
        container.addView(itemView);
        // Return the page
        return itemView;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }




}
