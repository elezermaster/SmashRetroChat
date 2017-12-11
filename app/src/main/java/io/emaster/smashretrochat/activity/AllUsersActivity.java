package io.emaster.smashretrochat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.emaster.smashretrochat.ChatRetroOfflineApp;
import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.adapter.TabsPagerAdapter;
import io.emaster.smashretrochat.adapter.UsersChatAdapter;
import io.emaster.smashretrochat.model.Users;

/**
 * Created by elezermaster on 07/09/2017.
 */

public class AllUsersActivity extends AppCompatActivity{

    Toolbar mToolbar;
    RecyclerView allUsersRecycler;
    DatabaseReference allUsersDR;
    public Context ctx;
    UsersChatAdapter mUsersChatAdapter;

    String user_name ="";
    String user_online ="offline";
    String user_status ="";
    String user_email ="";
    String user_id_email = "";
    String user_date_created ="";
    String user_image_url ="";
    String user_thumb_image ="";
    String user_last_online = "";
    String user_lat = "";
    String user_lng = "";
    String user_place ="";
    String current_user_id ="";
    String before_edit_status ="";

    ViewPager mViewPager;
    TabLayout mTabLayout;
    TabsPagerAdapter mTabsPagerAdapter;

    static List<Users> emptyListChat;// =new ArrayList<Users>();

    FirebaseAuth mAuth;
    DatabaseReference usersReference;
    FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        //get user info
        mAuth = FirebaseAuth.getInstance(); // important Call
        //Fetch the Display name of current User
        user = mAuth.getCurrentUser();



        ctx = getApplicationContext();
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mViewPager = (ViewPager)findViewById(R.id.show_data_view_pager);
//        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(mTabsPagerAdapter);
//        mTabLayout = (TabLayout)findViewById(R.id.show_data_tabs);
//        mTabLayout.setupWithViewPager(mViewPager);


        allUsersRecycler = (RecyclerView) findViewById(R.id.rv_all_users_rv);
        allUsersRecycler.hasFixedSize();
        allUsersRecycler.setLayoutManager(new LinearLayoutManager(this));

        allUsersDR = FirebaseDatabase.getInstance().getReference().child("Users");
        allUsersDR.keepSynced(true);

        ////////////////////////////////
       // View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

       // recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_news);
       // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //firebaseClient =  new FirebaseClient(getActivity(), ReferenceUrl.FIREBASE_CHAT_URL, recyclerView );

        //firebaseClient.refreshData();
        // Initialize adapter


        emptyListChat = getAllUsers();
        mUsersChatAdapter =new UsersChatAdapter(ctx,emptyListChat);


        //getAllUsers();
        Log.d("ALL"," size of users: " +emptyListChat.size());

        mUsersChatAdapter.notifyDataSetChanged();
        allUsersRecycler.setAdapter(mUsersChatAdapter);


        // Toast.makeText(getActivity().getApplicationContext(), ""+ user.getProviders().toString(), Toast.LENGTH_LONG).show();
        // Log.i("FIRE"," onCreateView user: " +user.getProviders().toString());


        //allUsersRecycler.setAdapter(mUsersChatAdapter);

        // Set adapter to recyclerView
        //mUsersFireChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mUsersFireChatRecyclerView.setHasFixedSize(true);
        //mUsersFireChatRecyclerView.setAdapter(mUsersChatAdapter);

    }

    private  List<Users> getAllUsers() {
        final List<Users> emptyListChatTemp=new ArrayList<Users>();
        //emptyListChat=new ArrayList<Users>();

        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        final String curr_user_id = preferences.getString("USER_ID", null);


        allUsersDR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Users user = new Users();
                    user_name = ds.child("user_name").getValue().toString();
                    user_status = ds.child("user_status").getValue().toString();
                    user_email = ds.child("user_email").getValue().toString();
                    user_id_email = ds.child("user_id_email").getValue().toString();
                    user_date_created = ds.child("user_date_created").getValue().toString();
                    user_last_online = ds.child("user_last_online").getValue().toString();
                    user_image_url = ds.child("user_image_url").getValue().toString();
                    user_thumb_image = ds.child("user_thumb_image").getValue().toString();
                    user_lat = ds.child("user_lat").getValue().toString();
                    user_lng = ds.child("user_lng").getValue().toString();
                    user_place = ds.child("user_place").getValue().toString();


                    if(ds.hasChild("online")) {
                        user_online =  ds.child("online").getValue().toString();

                        if(user_online.equals("true")){
                            user.setUser_online("true");
                        }else if(user_online.equals("offline") || user_online.equals("false")){
                            user.setUser_online("offline");
                        }else{
                           String timeStamp=  ds.child("online").getValue().toString();
                            long time =  Long.valueOf(timeStamp).longValue();

                            String s = DateFormat.getDateTimeInstance().format(new Date(time));
                            //user.setUser_online(s);

                            //String str = "1427241600000";
                            //SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                            //Date date = new Date(Long.parseLong(str));




                            long milliSeconds= Long.parseLong(user_online);
                            //Then create SimpleDateFormat
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            //Now convert your millisecond timestamp to ur sdf format
                            String dateAsString = sdf.format (milliSeconds);
                            //After that you can parse it to ur Date variable
                            user.setUser_online(dateAsString);
                            ///Date date = sdf.parse(dateAsString);
                        }
                    }

                    user.setUser_name(user_name);
                    user.setUser_status(user_status);
                    user.setUser_email(user_email);
                    user.setUser_id_email(user_id_email);
                    user.setUser_date_created(user_date_created);
                    user.setUser_last_online(user_last_online);
                    user.setUser_image_url(user_image_url);
                    user.setUser_thumb_image(user_thumb_image);
                    user.setUser_lat(user_lat);
                    user.setUser_lng(user_lng);
                    user.setUser_place(user_place);


                    if(curr_user_id!=null) {
                        if (!curr_user_id.equals(user_id_email)) {
                            //emptyListChat.add(user);
                            emptyListChatTemp.add(user);
                        }
                    }else {
                        emptyListChatTemp.add(user);
                    }


                    Log.d("User","added\n user_name :"+ user_name+
                            "\n user_status :"+ user_status+
                            "\n user_email :"+ user_email
                    );

                   // ArrayList<String> arrayUrls = new ArrayList<String>();
//                    if(ds.getValue(UserNearby.class).getImagesUrl()!=null){
//                        for(String url: ds.getValue(UserNearby.class).getImagesUrl()) {
//                            Log.i("URLS","getValue: "+url);
//                            arrayUrls.add(url);
//                        }
//                    }
                    //userNearby.setImagesUrl(arrayUrls );
                    //Log.i("UPDATES","user: " +userNearby.getName());
                    //users.add(userNearby);
                }







                //Log.d("User","key:::: "+ dataSnapshot.child("user_name").getKey());
                //Log.d("User","ref:::" + dataSnapshot.child("user_name").getRef().toString());
                //Log.d("User","val:::"+ dataSnapshot.child("user_name").getValue().toString());

//                user_name = dataSnapshot.child("user_name").getValue().toString();
//                before_edit_status = user_status = dataSnapshot.child("user_status").getValue().toString();
//                user_email = dataSnapshot.child("user_email").getValue().toString();
//                user_date_created = dataSnapshot.child("user_date_created").getValue().toString();
//                user_last_online = dataSnapshot.child("user_last_online").getValue().toString();
//                user_image_url = dataSnapshot.child("user_image_url").getValue().toString();
//                user_thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();
//                user_lat = dataSnapshot.child("user_lat").getValue().toString();
//                user_lng = dataSnapshot.child("user_lng").getValue().toString();
//                user_place = dataSnapshot.child("user_place").getValue().toString();


//                editor.putString("USER_NAME", user_name);
//                editor.putString("USER_STATUS", user_status);
//                editor.putString("USER_EMAIL", user_email);
//                editor.putString("USER_DATE_CREATED", user_date_created);
//                editor.putString("USER_LAST_ONLINE", user_last_online);
//                editor.putString("USER_IMAGE_URL", user_image_url);
//                editor.putString("USER_THUMB_IMAGE", user_thumb_image);
//                editor.putString("USER_LAT", user_lat);
//                editor.putString("USER_LNG", user_lng);
//                editor.putString("USER_PLACE", user_place);
//                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  emptyListChatTemp;
        //return emptyListChat;
    }

    @Override
    protected void onStart() {
        super.onStart();

//        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
//        String curr_user_online = preferences.getString("USER_ONLINE", null);
//        if(curr_user_online.equals("FALSE")) {
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("USER_ONLINE", "TRUE");
//            editor.commit();
//
//            if (user != null) {
//                //LoggedIn_User_Name = user.getDisplayName();
//                //LoggedIn_User_Email = user.getEmail();
//
//
//                Log.d("LOGGED ONSTART", "user: " + user);
//
//
//                //Setting the tags for Current User.
//
//                String online_user_id = mAuth.getCurrentUser().getUid();
//
//                usersReference = FirebaseDatabase.getInstance().getReference()
//                        .child("Users").child(online_user_id);
//                usersReference.child("online").setValue(true);
//            }
//        }



//        FirebaseRecyclerAdapter<Users, AllUsersViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Users, AllUsersViewHolder>(
//                        Users.class,
//                        R.layout.all_users_display_item,
//                        AllUsersViewHolder.class,
//                        allUsersDR
//                ) {
//                    @Override
//                    protected void populateViewHolder(AllUsersViewHolder viewHolder, Users model, int position) {
//                        viewHolder.setUser_name(model.getUser_name());
//                        viewHolder.setUser_email(model.getUser_status());
//                        viewHolder.setUser_image_url(getApplicationContext(), model.getUser_image_url());
//                    }
//                };

        //allUsersRecycler.setAdapter(firebaseRecyclerAdapter);
        //mUsersChatAdapter.notifyDataSetChanged();
        //allUsersRecycler.setAdapter(mUsersChatAdapter);

    }

    public static class AllUsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUser_name(String user_name) {
            TextView textViewName = (TextView) mView.findViewById(R.id.chat_persion_name);
            textViewName.setText(user_name);
        }

        public void setUser_email(String user_email) {
            TextView textViewEmail = (TextView) mView.findViewById(R.id.chat_persion_email);
            textViewEmail.setText(user_email);
        }

        public void setUser_image_url(Context ctx, String user_image_url) {
            ImageView imageViewUser = (ImageView)mView.findViewById(R.id.chat_persion_image);
            Picasso.with(ctx).load(user_image_url).into(imageViewUser);
        }

        public void setUser_thumb_url(Context ctx, String user_thumb_url) {
            ImageView imageViewUser = (ImageView)mView.findViewById(R.id.chat_persion_image);
            Picasso.with(ctx).load(user_thumb_url).into(imageViewUser);
        }
    }

//    @Override
//    public void onResume()
//    {
//        super.onResume();
//
//        ChatRetroOfflineApp myApp = (ChatRetroOfflineApp)this.getApplication();
//        if (myApp.wasInBackground)
//        {
//            //Do specific came-here-from-background code
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("USER_ONLINE", "TRUE");
//            editor.commit();
//
//            //user = mAuth.getCurrentUser();
//            if (user != null) {
//                //LoggedIn_User_Name = user.getDisplayName();
//                //LoggedIn_User_Email = user.getEmail();
//
//
//                Log.d("LOGGED ONSTART", "user: " + user);
//
//
//                //Setting the tags for Current User.
//
//                String online_user_id = mAuth.getCurrentUser().getUid();
//
//                usersReference = FirebaseDatabase.getInstance().getReference()
//                        .child("Users").child(online_user_id);
//                usersReference.child("online").setValue(true);
//            }
//        }
//
//        myApp.stopActivityTransitionTimer();
//    }

//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        ((ChatRetroOfflineApp)this.getApplication()).startActivityTransitionTimer();
//    }
}
