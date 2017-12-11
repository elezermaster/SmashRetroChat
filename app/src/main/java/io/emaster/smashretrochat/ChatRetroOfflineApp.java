package io.emaster.smashretrochat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import io.emaster.smashretrochat.helper.ApplicationLifeCycleHandler;

/**
 * Created by elezermaster on 28/09/2017.
 */

public class ChatRetroOfflineApp extends Application{

    DatabaseReference UsersReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //load pic offline
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setIndicatorsEnabled(true);
        Picasso.setSingletonInstance(built);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            String online_user_id = mAuth.getCurrentUser().getUid();

            UsersReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(online_user_id);
            UsersReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UsersReference.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);

                    //UsersReference.child("online").setValue(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }


    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                ChatRetroOfflineApp.this.wasInBackground = true;

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("USER_ONLINE", "TRUE");
                editor.commit();

                //user = mAuth.getCurrentUser();
                if (currentUser != null) {
                    mAuth = FirebaseAuth.getInstance();
                    String online_user_id = mAuth.getCurrentUser().getUid();
                    if(online_user_id!= null) {
                        UsersReference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(online_user_id);
                        UsersReference.child("online").setValue(true);
                    }
                }
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        this.wasInBackground = false;
    }


}
