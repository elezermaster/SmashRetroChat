package io.emaster.smashretrochat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.model.Friends;

/**
 * Created by elezermaster on 28/09/2017.
 */

public class ThreeFragment extends Fragment {

    RecyclerView mFriendList;
    DatabaseReference friendsRef;
    DatabaseReference usersRef;
    DatabaseReference friendsReqRef;

    FirebaseAuth mAuth;
    String online_user_id;
    View mMainView;

    public ThreeFragment() {
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
        mMainView =  inflater.inflate(R.layout.fragment_tree, container, false);
        mFriendList = (RecyclerView)mMainView.findViewById(R.id.requestsRecyclerView);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!= null) {
            online_user_id = mAuth.getCurrentUser().getUid();
            friendsRef = FirebaseDatabase.getInstance().getReference()
                    .child("Friend_Request").child(online_user_id);//.child("request_type");
            friendsRef.keepSynced(true);
        }


        usersRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        usersRef.keepSynced(true);
        friendsReqRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        //.child(receiver_user_name).child(sender_user_name)
        //.child("request_type").setValue("friend");
        friendsReqRef.keepSynced(true);
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();


        if(friendsRef!=null){
        FirebaseRecyclerAdapter<Friends, TwoFragment.FriendsViewHolder> friendsFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Friends, TwoFragment.FriendsViewHolder>
                        (
                                Friends.class,
                                R.layout.all_users_display_item,
                                TwoFragment.FriendsViewHolder.class,
                                friendsRef
                        ) {
                    @Override
                    protected void populateViewHolder(final TwoFragment.FriendsViewHolder viewHolder, Friends model, int position) {

                        //Log.d("USER_ID_POP", model.getUser_id_email());
                        Log.d("USER_EMAIL_POP", "" + model.getDate());

                        Log.d("USER_REF_POSITION", "" + position);
                        final String list_user_id = getRef(position).getKey();

                        Log.d("GET_ROOT", "" + getRef(position).getRoot());
                        Log.d("GET_DB", "" + getRef(position).getDatabase());
                        Log.d("GET_PARENT", "" + getRef(position).getParent());
                        Log.d("USER_REF_ID", "" + list_user_id);
                        //final String list_user_status = getRef(position).child(list_user_id).getRef().getKey().toString();//.child("request_type").;//.child("request_type").toString();//getKey();
                        //Log.d("USER_REF", list_user_status);
                        //friendsReqRef = FirebaseDatabase.getInstance().getReference();
                        //final String key = getRef(position).child(list_user_id).child(list_user_status)
                        //.child("request_type").toString();//.getKey();//.equals("friend")) {//.setValue("friend");
                        //final String[] status = new String[1];
                        friendsReqRef.child(list_user_id)//.child(list_user_status).child("request_type")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                            // if(dataSnapshot.getValue() != null) {
                                            if (ds.child("request_type").getValue() != null) {

                                                //request_type == "received"
                                                if (ds.child("request_type").getValue().toString().equals("received")) {
                                                    Log.d("parent", ds.getKey());
                                                    String user_id = ds.getKey();
                                                    Log.d("status", ds.child("request_type").getValue().toString()); //status
                                                    Log.d("request_type", ds.child("request_type").getKey().toString());
                                                    String status1 = ds.child("request_type").getValue().toString();
                                                    viewHolder.setDate(status1);


                                                    usersRef.child(user_id).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String userName = dataSnapshot.child("user_name").getValue().toString();
                                                            String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();
                                                            //String userStatus = dataSnapshot.


                                                            viewHolder.setUserName(userName);
                                                            viewHolder.setThumbImage(thumbImage, getContext());
                                                            //viewHolder.setDate(status[0]);

                                                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }


                                                //ds.getKey() = list_user_id
                                                //usersRef = FirebaseDatabase.getInstance().getReference()
                                                //        .child("Users");
                                                //usersRef.keepSynced(true);
//                                                usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        String userName = dataSnapshot.child("user_name").getValue().toString();
//                                                        String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();
//                                                        //String userStatus = dataSnapshot.
//
//
//                                                        viewHolder.setUserName(userName);
//                                                        viewHolder.setThumbImage(thumbImage, getContext());
//                                                        //viewHolder.setDate(status[0]);
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });


                                            }
//                                       //////////////////////
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

//                        usersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String userName = dataSnapshot.child("user_name").getValue().toString();
//                                String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();
//                                //String userStatus = dataSnapshot.
//
//
//                                viewHolder.setUserName(userName);
//                                viewHolder.setThumbImage(thumbImage, getContext());
//                                //viewHolder.setDate(status[0]);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });


                    }


                };
            mFriendList.setAdapter(friendsFirebaseRecyclerAdapter);
    }


    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDate(String date){
            TextView sinceFriendDate = (TextView)mView.findViewById(R.id.chat_persion_email);
            sinceFriendDate.setText(date);
        }

        public  void setUserName(String userName){
            TextView userNameDisplay = (TextView)mView.findViewById(R.id.chat_persion_name);
            userNameDisplay.setText(userName);
        }

        public  void setThumbImage(String thumbImage, final Context context) {

            ImageView imageView = (ImageView)mView.findViewById(R.id.chat_persion_image);

            Picasso.with(context)
                    // .load(fireChatUser.getUser_image_url()) //original image
                    .load(thumbImage)  //thumb img
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_user)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
//                            Picasso.with(context)
//                                    // .load(fireChatUser.getUser_image_url()) //original image
//                                    .load(fireChatUser.getUser_thumb_image())  //thumb img
//                                    .placeholder(avatarDrawable)
//                                    .into(thumbImage);
                        }
                    });
        }
    }
}