package io.emaster.smashretrochat.fragment;

/**
 * Created by elezermaster on 28/09/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.adapter.UsersChatAdapter;
import io.emaster.smashretrochat.model.Users;

public class OneFragment extends Fragment{

    RecyclerView mFriendList;
    DatabaseReference friendsRef;
    DatabaseReference usersRef;
    DatabaseReference friendsReqRef;
    DatabaseReference getUserInfoRef;
    DatabaseReference allFriendsDR;

    FirebaseAuth mAuth;
    String online_user_id;
    View mMainView;

    Context ctx;
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

    static List<Users> emptyListFriends;// =new ArrayList<Users>();

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = getContext();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!= null) {
            online_user_id = mAuth.getCurrentUser().getUid();
        }
        emptyListFriends = getAllFriends();

        //allFriendsDR = FirebaseDatabase.getInstance().getReference().child("Users");
        //allFriendsDR.keepSynced(true);


        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_one, container, false);
        // 1. get a reference to recyclerView
        mFriendList = (RecyclerView)mMainView.findViewById(R.id.allUsersRecyclerView);


        //friendsRef = FirebaseDatabase.getInstance().getReference()
        //        .child("Friend_Request").child(online_user_id);//.child("request_type");
        //friendsRef.keepSynced(true);

        //usersRef = FirebaseDatabase.getInstance().getReference()
        //        .child("Users");
        //usersRef.keepSynced(true);
        //friendsReqRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        //.child(receiver_user_name).child(sender_user_name)
        //.child("request_type").setValue("friend");
        //friendsReqRef.keepSynced(true);
        // 2. set layoutManger
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));


//        emptyListChat = getAllUsers();
        // 3. create an adapter
//        mUsersChatAdapter =new UsersChatAdapter(ctx,emptyListChat);
//        // 4. set adapter
//        mFriendList.setAdapter(mUsersChatAdapter);
//        // 5. set item animator to DefaultAnimator
//        mFriendList.setItemAnimator(new DefaultItemAnimator());

        //mFriendList.setAdapter(friendsFirebaseRecyclerAdapter);
        return mMainView;


        //return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        mUsersChatAdapter =new UsersChatAdapter(ctx,emptyListFriends);
        // 4. set adapter
        mFriendList.setAdapter(mUsersChatAdapter);
        // 5. set item animator to DefaultAnimator
        mFriendList.setItemAnimator(new DefaultItemAnimator());


    }

    public List<Users> getAllFriends() {
        final List<Users> emptyListChat=new ArrayList<Users>();
        final List<Users> emptyListChatTemp=new ArrayList<Users>();
        final List<String> friendsList = new ArrayList<>();

        //emptyListChat=new ArrayList<Users>();

        //SharedPreferences preferences =  ctx.getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        //final String curr_user_id = preferences.getString("USER_ID", null);
        Log.d("current_id", ""+online_user_id);


        if(online_user_id!= null) {
            friendsReqRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
            friendsReqRef.child(online_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("FRIEND_DS", ds.getValue().toString());
                        //{request_type=friend, from_date=2017-10-10}
                        Log.d("FRIEND_DS", ds.getKey().toString());
                        String friend_id = ds.getKey().toString();
                        //ZNEd2Wqq69NlWqqjc27vW3P0Lhy2
                        Log.d("request_type", ds.child("request_type").getValue().toString());
                        if (ds.child("request_type").getValue() != null) {
                            if (ds.child("request_type").getValue().toString().equals("friend")) {
                                //ds.getKey().toString();
                                friendsList.add(friend_id);
                                Users user = new Users();
                                user.setUser_id_email(friend_id);
                                emptyListChatTemp.add(user);
                            }
                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        allFriendsDR = FirebaseDatabase.getInstance().getReference().child("Users");
        allFriendsDR.keepSynced(true);

        if(emptyListChatTemp.size()>0) {
            for (final Users user : emptyListChatTemp) {

                allFriendsDR.child(user.getUser_id_email()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot ds) {

                        //for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            //Users user = new Users();
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


                            if (ds.hasChild("online")) {
                                user_online = ds.child("online").getValue().toString();

                                if (user_online.equals("true")) {
                                    user.setUser_online("true");
                                } else if (user_online.equals("offline") || user_online.equals("false")) {
                                    user.setUser_online("offline");
                                } else {
                                    String timeStamp = ds.child("online").getValue().toString();
                                    long time = Long.valueOf(timeStamp).longValue();

                                    String s = DateFormat.getDateTimeInstance().format(new Date(time));
                                    //user.setUser_online(s);

                                    //String str = "1427241600000";
                                    //SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                    //Date date = new Date(Long.parseLong(str));


                                    long milliSeconds = Long.parseLong(user_online);
                                    //Then create SimpleDateFormat
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                                    //Now convert your millisecond timestamp to ur sdf format
                                    String dateAsString = sdf.format(milliSeconds);
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


                            if (!online_user_id.equals(user_id_email)) {
                                //emptyListChat.add(user);
                                emptyListChat.add(user);
                            }


                            Log.d("User", "added\n user_name :" + user_name +
                                    "\n user_status :" + user_status +
                                    "\n user_email :" + user_email
                            );

                       // }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }else {
            Log.d("SIZE", ""+ emptyListChatTemp.size()
            );
        }

        return  emptyListChat;
        //return emptyListChat;
    }

}
