package io.emaster.smashretrochat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
//import com.vansuita.materialabout.views.CircleImageView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import de.hdodenhof.circleimageview.CircleImageView;
import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.helper.GetDateTime;
import io.emaster.smashretrochat.login.LoginActivity;
import io.emaster.smashretrochat.model.Users;

/**
 * Created by elezermaster on 18/09/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    Context ctx;
    Toolbar mToolbar;
    CircleImageView userImageCiv;
    TextView userNameTv;
    TextView userStatusTv;
    TextView userStatusTv2;

    String CURRENT_STATE_FRIEND = "not_friend";
    DatabaseReference FriendRequestRef;
    DatabaseReference NotificationsRef;
    DatabaseReference getUserInfoRef;

    String sender_user_name;// = preferences.getString("USER_ID", null);
    String sender_user_email;// = preferences.getString("USER_EMAIL", null);
    String sender_user_first_name;// = preferences.getString("USER_NAME", null);
    String receiver_user_name;// = user.getUser_id_email();// id to send him request
    String receiver_user_email;// = user.getUser_email();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FriendRequestRef =  FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        FriendRequestRef.keepSynced(true);
        NotificationsRef =  FirebaseDatabase.getInstance().getReference().child("Notifications");
        NotificationsRef.keepSynced(true);

        setContentView(R.layout.user_activity_profile);

        ctx = getApplicationContext();
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("All Users");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(mToolbar);

        //chatIntent.putExtra("PASS_USER_NAME",mCurrentUserName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Users customerObj = getIntent().getExtras().getParcelable("PASS_USER");
        String customerObjname = getIntent().getExtras().getString("PASS_USER_NAME");//.toString();
        Users user= getIntent().getExtras().getParcelable("PASS_USER");
        String user_Id = getIntent().getExtras().getString("PASS_USER_ID");
        if(user!=null) {
            getSupportActionBar().setTitle(user.getUser_name());
        }else if (customerObjname!=null){
            getSupportActionBar().setTitle(customerObjname);
        }else{
            getSupportActionBar().setTitle(user_Id);
        }
        //Toast.makeText(getApplicationContext(), "data user\n "+user.getUser_name()+"\n"+user.getUser_status(), Toast.LENGTH_LONG).show();
        //getSupportActionBar().setTitle(customerObjInToClass.getUser_name());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Log.d("USER_ID", ""+ user_Id);
        if(user_Id!=null) {
            //receiver_user_name = user_Id;
            getUserInfoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_Id);

            getUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users currentUser = new Users();
                    String user_name = dataSnapshot.child("user_name").getValue().toString();
                    String user_status = dataSnapshot.child("user_status").getValue().toString();
                    String user_email = dataSnapshot.child("user_email").getValue().toString();
                    String user_id_email = dataSnapshot.child("user_id_email").getValue().toString();
                    String user_date_created = dataSnapshot.child("user_date_created").getValue().toString();
                    String user_last_online = dataSnapshot.child("user_last_online").getValue().toString();
                    String user_image_url = dataSnapshot.child("user_image_url").getValue().toString();
                    String user_thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();
                    String user_lat = dataSnapshot.child("user_lat").getValue().toString();
                    String user_lng = dataSnapshot.child("user_lng").getValue().toString();
                    String user_place = dataSnapshot.child("user_place").getValue().toString();



                    currentUser.setUser_name(user_name);
                    currentUser.setUser_status(user_status);
                    currentUser.setUser_email(user_email);
                    currentUser.setUser_id_email(user_id_email);
                    currentUser.setUser_date_created(user_date_created);
                    currentUser.setUser_last_online(user_last_online);
                    currentUser.setUser_image_url(user_image_url);
                    currentUser.setUser_thumb_image(user_thumb_image);
                    currentUser.setUser_lat(user_lat);
                    currentUser.setUser_lng(user_lng);
                    currentUser.setUser_place(user_place);

                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("USER_NAME", user_name);
                    editor.putString("USER_STATUS", user_status);
                    editor.putString("USER_ID", user_id_email);
                    editor.putString("USER_EMAIL", user_email);
                    editor.putString("USER_DATE_CREATED", user_date_created);
                    editor.putString("USER_LAST_ONLINE", user_last_online);
                    editor.putString("USER_IMAGE_URL", user_image_url);
                    editor.putString("USER_THUMB_IMAGE", user_thumb_image);
                    editor.putString("USER_LAT", user_lat);
                    editor.putString("USER_LNG", user_lng);
                    editor.putString("USER_PLACE", user_place);
                    editor.commit();
                    Log.d("FETCHED", "Curr User: " + user_name +"\n"+
                            user_status +"\n"+
                            user_email +"\n"+
                            user_id_email +"\n"+
                            user_date_created +"\n"+
                            user_last_online +"\n"+
                            user_image_url +"\n"+
                            user_thumb_image +"\n"+
                            user_lat +"\n"+
                            user_lng +"\n"+
                            user_place +"\n"
                    );

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        sender_user_name = preferences.getString("USER_ID", null);
        sender_user_email = preferences.getString("USER_EMAIL", null);
        sender_user_first_name = preferences.getString("USER_NAME", null);
        if(user!=null) {
            receiver_user_name = user.getUser_id_email();// id to send him request
        }else {

        }
        //receiver_user_email = user.getUser_email();

        Drawable avatarDrawable= ContextCompat.getDrawable(ctx,R.drawable.headshot_7);
        userImageCiv = (CircleImageView)findViewById(R.id.userPhotoProfile);
        Picasso.with(ctx)
                // .load(fireChatUser.getUser_image_url()) //original image
                .load(user.getUser_thumb_image())  //thumb img
                .placeholder(avatarDrawable)
                .into(userImageCiv);
        userNameTv = (TextView)findViewById(R.id.userFirstNameProfile);
        userNameTv.setText(user.getUser_name());
        userStatusTv = (TextView)findViewById(R.id.userStatusProfile);
        //userStatusTv.setText(user.getUser_status());


        userStatusTv2 = (TextView)findViewById(R.id.tvUserStatus);
        userStatusTv2.setText(user.getUser_status());


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_user);

        //CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        //p.setAnchorId(View.NO_ID);
        //p.width = 0;
        //p.height = 0;
        //fab.setLayoutParams(p);
        //fab.setVisibility(View.GONE);
        if(sender_user_name == null){
            //fab.hide();
            //fab.setVisibility(View.GONE);
            fab.setTag("invisible");
            fab.setImageResource(R.drawable.fab_invisibleuser);
        }else {
            //fab.show();
            //fab.setVisibility(View.VISIBLE);
        }

        //if sent request set
        //view.setTag("remove");
        //fab.setImageResource(R.drawable.fab_removeuser);

        if(sender_user_name!=null) {
            FriendRequestRef.child(sender_user_name)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(receiver_user_name)) {
                                String request_type = dataSnapshot.child(receiver_user_name)
                                        .child("request_type").getValue().toString();
                                //sent or received
                                if (request_type.equals("sent")) {
                                    userStatusTv.setText("request sent");
                                    fab.setTag("remove");
                                    fab.setImageResource(R.drawable.fab_removeuser);
                                } else if (request_type.equals("friend")) {
                                    String from_date = dataSnapshot.child(receiver_user_name)
                                            .child("from_date").getValue().toString();
                                    userStatusTv.setText("friend from " + from_date);
                                    fab.setTag("remove");
                                    fab.setImageResource(R.drawable.fab_removeuser);
                                } else if (request_type.equals("received")) {
                                    userStatusTv.setText("request waiting for accept");
                                    fab.setTag("accept");
                                    fab.setImageResource(R.drawable.fab_acceptuser);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }


        Log.d("SENDER", ""+sender_user_name);
        Log.d("RECEIVER", ""+receiver_user_name);
//        FriendRequestRef.child(receiver_user_name)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        Log.d("Fsender:", ""+sender_user_name);
//                        Log.d("Freceiver:", ""+receiver_user_name);
//
//
//                        if(dataSnapshot.hasChild(sender_user_name)){
//                            String request_type = dataSnapshot.child(receiver_user_name)
//                                    .child("request_type").getValue().toString();
//                            //sent or received
//                            if(request_type.equals("sent")){
//                                fab.setTag("remove");
//                                fab.setImageResource(R.drawable.fab_removeuser);
//                            }else if(request_type.equals("friend")){
//                                fab.setTag("remove");
//                                fab.setImageResource(R.drawable.fab_removeuser);
//                            }else if(request_type.equals("accept")){
//                                fab.setTag("accept");
//                                fab.setImageResource(R.drawable.fab_acceptuser);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //if(view.getResources().getResourceEntryName(view.getId()).equals("fab_add_user")){
                //    fab.setImageResource(R.drawable.fab_removeuser);
                //}

                if(view.getTag().toString().equals("add")){
                    Log.d("view.getTag():", ""+view.getTag());
                    //view.setTag("remove");
                    //fab.setImageResource(R.drawable.fab_removeuser);
                    Log.d("sender:", ""+sender_user_name);
                    Log.d("receiver:", ""+receiver_user_name);
                    FriendRequestRef.child(sender_user_name).child(receiver_user_name)
                    .child("request_type").setValue("sent")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FriendRequestRef.child(receiver_user_name).child(sender_user_name)
                                        .child("request_type").setValue("received")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {

                                                    userStatusTv.setText("requested");
                                                    view.setTag("remove");
                                                    fab.setImageResource(R.drawable.fab_removeuser);

                                                    HashMap<String, String> notificationsData = new HashMap<String, String>();
                                                    notificationsData.put("from", sender_user_name);
                                                    notificationsData.put("type", "request");
                                                    NotificationsRef.child(receiver_user_name).push().setValue(notificationsData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        //TODO: set notification
                                                                        Toast.makeText(getApplicationContext(), "notification success\n ", Toast.LENGTH_LONG).show();
                                                                        userStatusTv.setText("notification requested");
                                                                        //view.setTag("remove");
                                                                        //fab.setImageResource(R.drawable.fab_removeuser);


                                                                        sendFriendRequestNotification(receiver_user_email, "friend request secondly "+ sender_user_first_name);
                                                                    }else{
                                                                        Log.d("NOTY", "sending by onesignal");
                                                                        sendFriendRequestNotification(receiver_user_email, "friend request from "+ sender_user_name);
                                                                    }
                                                                }
                                                            });



                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
                else if(view.getTag().toString().equals("remove")){
                    //cancel friend request
                    FriendRequestRef.child(sender_user_name).child(receiver_user_name)
                            .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FriendRequestRef.child(receiver_user_name).child(sender_user_name)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    userStatusTv.setText("request removed");
                                                    view.setTag("add");
                                                    fab.setImageResource(R.drawable.fab_adduser);
                                                }
                                            }
                                        });
                            }
                        }
                    });

//                    Log.d("view.getTag():", ""+view.getTag());
//                    view.setTag("add");
//                    fab.setImageResource(R.drawable.fab_adduser);
                }else if(view.getTag().toString().equals("accept")){
                    Log.d("view.getTag():", ""+view.getTag());
                    //get date time

                    final String currentDate = GetDateTime.getCurrentDate();
                    //view.setTag("remove");
                    //fab.setImageResource(R.drawable.fab_removeuser);
                    Log.d("sender:", ""+sender_user_name);
                    Log.d("receiver:", ""+receiver_user_name);
                    FriendRequestRef.child(sender_user_name).child(receiver_user_name)
                            .child("request_type").setValue("friend")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FriendRequestRef.child(sender_user_name).child(receiver_user_name)
                                                .child("from_date").setValue(currentDate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        userStatusTv.setText("friend accepted");
                                                        view.setTag("remove");
                                                        fab.setImageResource(R.drawable.fab_removeuser);
                                                    }
                                                });
                                    }
                                }
                            });
                    //FriendRequestRef =  FirebaseDatabase.getInstance().getReference().child("Friend_Request");
                    FriendRequestRef.child(receiver_user_name).child(sender_user_name)
                            .child("request_type").setValue("friend")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FriendRequestRef.child(receiver_user_name).child(sender_user_name)
                                                .child("from_date").setValue(currentDate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //get calculate number of friesnds
                                                        Toast.makeText(getApplicationContext(), "friend "+receiver_user_name +" accepted", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
                else if(view.getTag().toString().equals("invisible")){
                    Snackbar.make(view, "please log in", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
//

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab_chat_user = (FloatingActionButton) findViewById(R.id.fab_chat_user);
        if(sender_user_name==null){
            fab_chat_user.setTag("invisible");
            fab_chat_user.setImageResource(R.drawable.fab_chat_invisible);
        }
        fab_chat_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().toString().equals("hollow")){
                    //Log.d("view.getTag():", ""+view.getTag());
                    //view.setTag("full");
                    //fab_like_user.setImageResource(R.drawable.fab_like_filled);
                }
                else if(view.getTag().toString().equals("full")){
                    //Log.d("view.getTag():", ""+view.getTag());
                    //view.setTag("hollow");
                    //fab_like_user.setImageResource(R.drawable.fab_like);
                }
                else if(view.getTag().toString().equals("invisible")){
                    Snackbar.make(view, "please log in", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        final FloatingActionButton fab_like_user = (FloatingActionButton) findViewById(R.id.fab_like_user);
        if(sender_user_name==null){
            fab_like_user.setTag("invisible");
            fab_like_user.setImageResource(R.drawable.fab_like_invisible);
        }
        fab_like_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag().toString().equals("hollow")){
                    Log.d("view.getTag():", ""+view.getTag());
                    view.setTag("full");
                    fab_like_user.setImageResource(R.drawable.fab_like_filled);
                }
                else if(view.getTag().toString().equals("full")){
                    Log.d("view.getTag():", ""+view.getTag());
                    view.setTag("hollow");
                    fab_like_user.setImageResource(R.drawable.fab_like);
                }
                else if(view.getTag().toString().equals("invisible")){
                    Snackbar.make(view, "please log in", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ONLINE", "TRUE");
        editor.commit();
    }

    private void sendFriendRequestNotification(final String send_email, final String message){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);


                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        //REST API Key
                        //YmUxNjRiZDMtMzk5NS00YjVkLTkwNGUtMjMyNGU5ZjgyNjA4
                        con.setRequestProperty("Authorization", "Basic YmUxNjRiZDMtMzk5NS00YjVkLTkwNGUtMjMyNGU5ZjgyNjA4");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                //OneSignal App ID
                                //4b5690e9-740c-484d-b7ac-d04c3e3151b3
                                + "\"app_id\": \"4b5690e9-740c-484d-b7ac-d04c3e3151b3\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \""+ message +"\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);
                        Log.d("Notification", "strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);
                        Log.d("Notification", "httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);
                        Log.d("Notification", "jsonResponse:\n" + jsonResponse);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}
