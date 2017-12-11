package io.emaster.smashretrochat.helper;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by elezermaster on 10/10/2017.
 */

public class ApplicationLifeCycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifeCycleHandler.class.getSimpleName();
    private static boolean isInBackground = false;

    FirebaseAuth mAuth;
    DatabaseReference usersReference;
    FirebaseUser user;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //get user info
        mAuth = FirebaseAuth.getInstance(); // important Call
        //Fetch the Display name of current User
        user = mAuth.getCurrentUser();

        String online_user_id = "";
        if(user!=null){
             online_user_id = user.getUid();
        }else{
            mAuth.signOut();
        }

        usersReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(online_user_id);
        usersReference.keepSynced(true);

    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

        if(isInBackground){
            Log.d(TAG, "app went to foreground");
            isInBackground = false;



            //Do specific came-here-from-background code
            //SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);

            //SharedPreferences.Editor editor = preferences.edit();
            //editor.putString("USER_ONLINE", "TRUE");
            //editor.commit();

            //user = mAuth.getCurrentUser();
            if (user != null) {
                //LoggedIn_User_Name = user.getDisplayName();
                //LoggedIn_User_Email = user.getEmail();


                Log.d("LOGGED ONSTART", "user: " + user);


                //Setting the tags for Current User.

                //String online_user_id = mAuth.getCurrentUser().getUid();

                //usersReference = FirebaseDatabase.getInstance().getReference()
                //        .child("Users").child(online_user_id);
                usersReference.child("online").setValue(true);
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.d(TAG, "app went to background");
            isInBackground = true;

            if (user != null) {
                //LoggedIn_User_Name = user.getDisplayName();
                //LoggedIn_User_Email = user.getEmail();

                Log.d("ON_TRIM_MEMORY", "user: " + user);

                usersReference.child("online").setValue(false);
            }
        }
    }
}
