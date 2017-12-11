package io.emaster.smashretrochat.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import io.emaster.smashretrochat.activity.UploadInfoActivity;

/**
 * Created by elezermaster on 7/3/16.
 */
public class ItemImage {
    Bitmap image;
    String title;
    String url;

    public ItemImage(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public ItemImage(String name, String url) {
        super();
        this.title = name;
        this.url = url;
        if(!url.isEmpty()){
//                   StorageReference ref = FirebaseStorage.getInstance().getReference(url);
//
//        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                //This line of code is responsible for loading the Url Image
//                //Glide.with(context).load(uri).into(mImageView);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    //theBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    try {
//                        Glide.with((Activity) UploadInfoActivity.)
//                                .asBitmap()
//                                .load(stream.toByteArray())
//                                //.error(R.drawable.bg_people)
//                               // .transform(new CircleTransform(this))
//                               // .into(imageview);
//                                .into(100,100)
//                                .get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //Log.d("UPLOAD_FAIL", "Failure: " + keys.get(position));
//            }
//        });
        }
    }

    public ItemImage(Bitmap image, String title, String url) {
        this.image = image;
        this.title = title;
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
