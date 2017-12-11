package io.emaster.smashretrochat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.adapter.UsersChatAdapter;
import io.emaster.smashretrochat.helper.CircleTransform;
import io.emaster.smashretrochat.model.Chat_Data_Items;
import io.emaster.smashretrochat.model.Users;

import static io.emaster.smashretrochat.activity.AllUsersActivity.emptyListChat;

/**
 * Created by elezermaster on 25/08/2017.
 */

public class ChatActivity extends AppCompatActivity {

    Toolbar mToolbar;

    TextView person_name,person_email;
    RecyclerView recyclerViewChats;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRefUsers;
    DatabaseReference myRefFriends;
    DatabaseReference myRefMessages;

    FirebaseAuth mAuth;
    DatabaseReference usersReference;
    //public FirebaseRecyclerAdapter<Chat_Data_Items, Show_Chat_ViewHolder> mFirebaseAdapter;
    //ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;
    String online_user_id;
    static List<Users> userListChats;
    Context context;
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

    static List<Users> emptyListChatTemp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chats);

        mAuth = FirebaseAuth.getInstance(); // important Call
        online_user_id = mAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        myRefUsers = firebaseDatabase.getReference("Users");
        myRefUsers.keepSynced(true);
        myRefFriends = firebaseDatabase.getReference("Friend_Request").child(online_user_id);
        myRefFriends.keepSynced(true);
        myRefMessages = firebaseDatabase.getReference("Messages").child(online_user_id);
        myRefMessages.keepSynced(true);
        context = getApplicationContext();


        mToolbar = (Toolbar)findViewById(R.id.toolbar_chats);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewChats = (RecyclerView)findViewById(R.id.rv_chats);
        recyclerViewChats.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewChats.setLayoutManager(linearLayoutManager);

        userListChats = getChatUsers();
        mUsersChatAdapter =new UsersChatAdapter(context,userListChats);


        //getAllUsers();
        //Log.d("ALL"," size of users: " +emptyListChat.size());

        mUsersChatAdapter.notifyDataSetChanged();
        recyclerViewChats.setAdapter(mUsersChatAdapter);


        //progressBar = (ProgressBar) findViewById(R.id.show_chat_progressBar2);

        //Recycler View
        //recyclerView = (RecyclerView)findViewById(R.id.show_chat_recyclerView);

        //mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        //mLinearLayoutManager.setStackFromEnd(true);

        //recyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private List<Users> getChatUsers() {
       final List<Users> mListChatTemp=new ArrayList<Users>();
        //emptyListChat=new ArrayList<Users>();

        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        final String curr_user_id = preferences.getString("USER_ID", null);


        final List<String> usersId = new ArrayList<>();
        myRefMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    if(ds.exists()) {
                        final String user_id =ds.getKey();
                        Log.d("USERID", "id:"+ user_id);
                        //get user by iser id
                        usersId.add(user_id);

//                       String request_type =  ds.child("request_type").getValue().toString();
//
//                        if(request_type.equals("friend")){
//                            String timeStamp=  ds.child("from_date").getValue().toString();
//                            long time =  Long.valueOf(timeStamp).longValue();
//                            String s = DateFormat.getDateTimeInstance().format(new Date(time));
//                            long milliSeconds= Long.parseLong(timeStamp);
//                            //Then create SimpleDateFormat
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//                            String dateAsString = sdf.format (milliSeconds);
//
//                            ///Date date = sdf.parse(dateAsString);
//
//                            Users user = new Users();
//                            user_name = ds.child("user_name").getValue().toString();
//                            user_status = ds.child("user_status").getValue().toString();
//                            user_email = ds.child("user_email").getValue().toString();
//                            user_id_email = ds.child("user_id_email").getValue().toString();
//                            user_date_created = ds.child("user_date_created").getValue().toString();
//                            user_last_online = ds.child("user_last_online").getValue().toString();
//                            user_image_url = ds.child("user_image_url").getValue().toString();
//                            user_thumb_image = ds.child("user_thumb_image").getValue().toString();
//                            user_lat = ds.child("user_lat").getValue().toString();
//                            user_lng = ds.child("user_lng").getValue().toString();
//                            user_place = ds.child("user_place").getValue().toString();
//
//
//                            if(!curr_user_id.equals(user_id_email)) {
//                                //emptyListChat.add(user);
//
//
//                            user.setUser_online(dateAsString);
//                            user.setUser_name(user_name);
//                            user.setUser_status(user_status);
//                            user.setUser_email(user_email);
//                            user.setUser_id_email(user_id_email);
//                            user.setUser_date_created(user_date_created);
//                            user.setUser_last_online(user_last_online);
//                            user.setUser_image_url(user_image_url);
//                            user.setUser_thumb_image(user_thumb_image);
//                            user.setUser_lat(user_lat);
//                            user.setUser_lng(user_lng);
//                            user.setUser_place(user_place);
//                            emptyListChatTemp.add(user);
//
//                            }
//                        }



                    }






//                    Log.d("User","added\n user_name :"+ user_name+
//                            "\n user_status :"+ user_status+
//                            "\n user_email :"+ user_email
//                    );

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

        myRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    final Users user = new Users();
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
                        }else if(user_online.equals("offline")){
                            user.setUser_online("offline");
                        }else{
                            String timeStamp=  ds.child("online").getValue().toString();
                            long time =  Long.valueOf(timeStamp).longValue();

                            String s = DateFormat.getDateTimeInstance().format(new Date(time));

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

                    for(int i=0;i <usersId.size();i++) {
                        if (!curr_user_id.equals(user_id_email) && user_id_email.equals(usersId.get(i))) {
                            //emptyListChat.add(user);

                            //get the las message from conversation
                            myRefMessages.child(user_id_email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String getLastMessage ="";
                                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                                       //Log.d("MESSAGES:",""+ ds.child("message").getValue().toString());
                                       getLastMessage = ds.child("message").getValue().toString();
                                    }
                                    Log.d("MESSAGES_IF_NULL:",""+ getLastMessage
                                    +" \nfor: "+ user.getUser_name());
                                    user.setUser_last_message_from_current(getLastMessage);
                                    mListChatTemp.add(user);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Log.d("MESSAGES_IF_NULL2:",""+ user.getUser_last_message_from_current());
                            //emptyListChatTemp.add(user);
                        }
                    }

                    Log.d("User","added\n user_name :"+ user_name+
                            "\n user_status :"+ user_status+
                            "\n user_email :"+ user_email
                    );

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  mListChatTemp;
        //return emptyListChat;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        progressBar.setVisibility(ProgressBar.VISIBLE);
//        //Log.d("LOGGED", "Will Start Calling populateViewHolder : ");
//        //Log.d("LOGGED", "IN onStart ");
//
//
//        mFirebaseAdapter = new FirebaseRecyclerAdapter<Chat_Data_Items, Show_Chat_ViewHolder>(
//                Chat_Data_Items.class,
//                R.layout.chat_single_item,
//                Show_Chat_ViewHolder.class,
//                myRef) {
//
//            public void populateViewHolder(final Show_Chat_ViewHolder viewHolder, Chat_Data_Items model, final int position) {
//                //Log.d("LOGGED", "populateViewHolder Called: ");
//                progressBar.setVisibility(ProgressBar.INVISIBLE);
//
//                if (!model.getName().equals("Null")) {
//                    viewHolder.Person_Name(model.getName());
//                    viewHolder.Person_Image(model.getImage_Url());
//                    //viewHolder.Person_Email(model.getEmail());
//                    if(model.getEmail().equals(MainActivity.LoggedIn_User_Email))
//                    {
//                        //viewHolder.itemView.setVisibility(View.GONE);
//                        viewHolder.Layout_hide();
//
//                        //recyclerView.getChildAdapterPosition(viewHolder.itemView.getRootView());
//                        // viewHolder.itemView.set;
//
//
//                    }
//                    else
//                        viewHolder.Person_Email(model.getEmail());
//                }
//
//
//                //OnClick Item
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(final View v) {
//
//                        DatabaseReference ref = mFirebaseAdapter.getRef(position);
//                        ref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                String retrieve_name = dataSnapshot.child("Name").getValue(String.class);
//                                String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
//                                String retrieve_url = dataSnapshot.child("Image_URL").getValue(String.class);
//
//
//
//                                Intent intent = new Intent(getApplicationContext(), ChatConversationActivity.class);
//                                intent.putExtra("image_id", retrieve_url);
//                                intent.putExtra("email", retrieve_Email);
//                                intent.putExtra("name", retrieve_name);
//
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                });
//            }
//        };
//
//        recyclerView.setAdapter(mFirebaseAdapter);
//
//
//
//
//
//    }


//    //View Holder For Recycler View
//    public static class Show_Chat_ViewHolder extends RecyclerView.ViewHolder {
//        private final TextView person_name, person_email;
//        private final ImageView person_image;
//        private final LinearLayout layout;
//        final LinearLayout.LayoutParams params;
//
//        public Show_Chat_ViewHolder(final View itemView) {
//            super(itemView);
//            person_name = (TextView) itemView.findViewById(R.id.chat_persion_name);
//            person_email = (TextView) itemView.findViewById(R.id.chat_persion_email);
//            person_image = (ImageView) itemView.findViewById(R.id.chat_persion_image);
//            layout = (LinearLayout)itemView.findViewById(R.id.show_chat_single_item_layout);
//            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//
//
//        private void Person_Name(String title) {
//            // Log.d("LOGGED", "Setting Name: ");
//            person_name.setText(title);
//        }
//        private void Layout_hide() {
//            params.height = 0;
//            //itemView.setLayoutParams(params);
//            layout.setLayoutParams(params);
//
//        }
//
//
//        private void Person_Email(String title) {
//            person_email.setText(title);
//        }
//
//
//        private void Person_Image(String url) {
//
//            if (!url.equals("Null")) {
//                Glide.with(itemView.getContext())
//                        .load(url)
//                        .crossFade()
//                        .thumbnail(0.5f)
//                        .placeholder(R.drawable.loading)
//                        .bitmapTransform(new CircleTransform(itemView.getContext()))
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(person_image);
//            }
//
//        }
//
//
//    }

}

