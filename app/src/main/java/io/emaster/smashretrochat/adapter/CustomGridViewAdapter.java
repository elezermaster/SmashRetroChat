package io.emaster.smashretrochat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.model.ItemImage;


/**
 * Created by elezermaster on 7/3/16.
 */
public class CustomGridViewAdapter extends ArrayAdapter<ItemImage> {
    Context context;
    int layoutResourceId;
    ArrayList<ItemImage> data = new ArrayList<ItemImage>();

    public CustomGridViewAdapter(Context context, int layoutResourceId, ArrayList<ItemImage> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        ItemImage itemImage = data.get(position);
        holder.txtTitle.setText(itemImage.getTitle());
        //if BITMAP
        holder.imageItem.setImageBitmap(itemImage.getImage());
        //if URL
        //Glide.with(context).load(itemImage.getUrl()).into(holder.imageItem);
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
    }

    // alternative A
    //location.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);

    // alternative B
    //location.calculateDistance(startPoint, endPoint);

    // reduce the precision to 10,000m for privacy reasons
    //location.setBlurRadius(10000);

}
