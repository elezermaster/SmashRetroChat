package io.emaster.smashretrochat.helper;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.emaster.smashretrochat.R;


/**
 * Created by elezermaster on 04/09/16.
 */
public class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView imgView){
        if(url!=null || url.length()>0 ){
            Picasso.with(c).load(url).placeholder(R.drawable.headshot_7).into(imgView);
        }else {
            Picasso.with(c).load(R.drawable.headshot_7).into(imgView);
        }
    }
}
