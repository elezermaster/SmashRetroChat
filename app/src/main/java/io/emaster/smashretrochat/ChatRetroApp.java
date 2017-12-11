package io.emaster.smashretrochat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by elezermaster on 29/08/2017.
 */

public class ChatRetroApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}