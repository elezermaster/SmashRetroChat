package io.emaster.smashretrochat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.activity.ChatActivity;
import io.emaster.smashretrochat.activity.ChatRetroActivity;
import io.emaster.smashretrochat.activity.MainActivity;
import io.emaster.smashretrochat.activity.ProfileActivity;
import io.emaster.smashretrochat.adapter.AAdapter;
import io.emaster.smashretrochat.adapter.UsersChatAdapter;
import io.emaster.smashretrochat.model.Friends;
import io.emaster.smashretrochat.model.Users;

import static android.content.Context.WINDOW_SERVICE;
import static io.emaster.smashretrochat.adapter.AAdapter.TypeShow.Big;
import static io.emaster.smashretrochat.adapter.AAdapter.TypeShow.Grid;
import static io.emaster.smashretrochat.adapter.AAdapter.TypeShow.Small;


/**
 * Created by elezermaster on 28/09/2017.
 */


public class TwoFragment extends Fragment {

    public enum TypeShow {
        Grid,
        Small,
        Big }

    private RecyclerView smallRecycleView;
    private RecyclerView gridRecycleView;
    private RecyclerView bigRecycleView;

    private StaggeredGridLayoutManager gridLayoutManager;
    private LinearLayoutManager bigLayoutManager;
    private LinearLayoutManager smallLayoutManager;

    private AAdapter adapterGrid;
    private AAdapter adapterSmall;
    private AAdapter adapterBig;

    private ViewFlipper viewFlipper;

    private AAdapter.TypeShow typeShowList = Small;


    RecyclerView mFriendList;
    DatabaseReference friendsRef;
    DatabaseReference usersRef;
    DatabaseReference friendsReqRef;
    DatabaseReference getUserInfoRef;
    DatabaseReference allUsersDR;

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

    static ArrayList<Users> emptyListChat;// =new ArrayList<Users>();

    FloatingActionButton fabToSwitch;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                            // TODO: The navigation bar is visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            //hideNavigationBar()
                            Log.d("STATUS", "shown");
                            Toast.makeText(ctx, "Shown :", Toast.LENGTH_SHORT).show();

                        } else {
                            // TODO: The navigation bar is NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                            Log.d("STATUS", "hidden");
                            Toast.makeText(ctx, "Hidden :", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ctx =getContext();
        allUsersDR = FirebaseDatabase.getInstance().getReference().child("Users");
        allUsersDR.keepSynced(true);


        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_two, container, false);
        // 1. get a reference to recyclerView
        //mFriendList = (RecyclerView)mMainView.findViewById(R.id.friendsRecyclerView);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!= null){
            online_user_id = mAuth.getCurrentUser().getUid();
        }



        usersRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        usersRef.keepSynced(true);
        friendsReqRef = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
                //.child(receiver_user_name).child(sender_user_name)
                //.child("request_type").setValue("friend");
        friendsReqRef.keepSynced(true);
        // 2. set layoutManger
        //mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));



        emptyListChat = getAllUsers();

        viewFlipper = ((ViewFlipper)mMainView.findViewById(R.id.view_flipper));
        viewFlipper.setOutAnimation(ctx, R.anim.fade_out);//anim_fade_out_flip);
        viewFlipper.setInAnimation(ctx, R.anim.fade_in);//anim_fade_in_flip);

        smallRecycleView = ((RecyclerView)mMainView.findViewById(R.id.small_list_allusers));
        gridRecycleView = ((RecyclerView)mMainView.findViewById(R.id.grid_list_allusers));
        bigRecycleView = ((RecyclerView)mMainView.findViewById(R.id.big_list_allusers));

        smallRecycleView.setHasFixedSize(false);
        gridRecycleView.setHasFixedSize(false);
        bigRecycleView.setHasFixedSize(false);

        smallLayoutManager = new LinearLayoutManager(ctx);
        smallRecycleView.setLayoutManager(smallLayoutManager);
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridRecycleView.setLayoutManager(gridLayoutManager);
        gridRecycleView.setItemAnimator(null);
        bigLayoutManager = new LinearLayoutManager(ctx);
        bigRecycleView.setLayoutManager(bigLayoutManager);




//        emptyListChat = getAllUsers();
        // 3. create an adapter
//        mUsersChatAdapter =new UsersChatAdapter(ctx,emptyListChat);
//        // 4. set adapter
//        mFriendList.setAdapter(mUsersChatAdapter);
//        // 5. set item animator to DefaultAnimator
//        mFriendList.setItemAnimator(new DefaultItemAnimator());

        //mFriendList.setAdapter(friendsFirebaseRecyclerAdapter);
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //emptyListChat = getAllUsers();
        //mUsersChatAdapter =new UsersChatAdapter(ctx,emptyListChat);
        // 4. set adapter
        //mFriendList.setAdapter(mUsersChatAdapter);
        // 5. set item animator to DefaultAnimator

        adapterSmall = new AAdapter(ctx,emptyListChat,Small);
        smallRecycleView.setAdapter(adapterSmall);
        smallRecycleView.setItemAnimator(new DefaultItemAnimator());

        adapterGrid = new AAdapter(ctx,emptyListChat,Grid);
        gridRecycleView.setAdapter(adapterGrid);
        gridRecycleView.setItemAnimator(new DefaultItemAnimator());

        adapterBig = new AAdapter(ctx,emptyListChat,Big);
        bigRecycleView.setAdapter(adapterBig);
        bigRecycleView.setItemAnimator(new DefaultItemAnimator());

        //mFriendList.setItemAnimator(new DefaultItemAnimator());

        if (Build.VERSION.SDK_INT >= 11)
        {
            smallRecycleView.setVerticalScrollbarPosition(1);
            gridRecycleView.setVerticalScrollbarPosition(1);
            bigRecycleView.setVerticalScrollbarPosition(1);
        }

        smallRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // view.get
            }
        });


        fabToSwitch = ((MainActivity)getActivity()).getFab();
        fabToSwitch.setMaxWidth(24);
        if(typeShowList == Small){

            fabToSwitch.setImageResource(R.drawable.ic_if_switch_list_small);
        }
        else if(typeShowList == Big){
            fabToSwitch.setImageResource(R.drawable.ic_if_switch_list_big);
        }
        else if(typeShowList == Grid){
            fabToSwitch.setImageResource(R.drawable.ic_if_switch_list_grid);
        }else {

            fabToSwitch.setImageResource(R.drawable.ic_if_switch_list_big);
        }


        ((MainActivity)getActivity()).setFabClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Log.d("SELECTED", ""+ gridRecycleView.isSelected());
                Log.d("SELECTED", ""+ smallRecycleView.isSelected());
                Log.d("SELECTED", ""+ bigRecycleView.isSelected());

                smallRecycleView.setSelected(false);
                bigRecycleView.setSelected(false);
                gridRecycleView.setSelected(false);
                viewFlipper.showNext();

                if(typeShowList == Small){
                    typeShowList = Big;
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_if_switch_list_grid);

                    gridRecycleView.setVisibility(View.INVISIBLE);
                    bigRecycleView.setVisibility(View.INVISIBLE);
                }
                else if(typeShowList == Big){
                    typeShowList = Grid;
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_if_switch_list_small);

                    gridRecycleView.setVisibility(View.INVISIBLE);
                    smallRecycleView.setVisibility(View.INVISIBLE);
                }
                else {
                    typeShowList = Small;
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_if_switch_list_big);

                    bigRecycleView.setVisibility(View.INVISIBLE);
                    smallRecycleView.setVisibility(View.INVISIBLE);
                }
                }
        });





//        final FloatingActionButton fabSwitch = (FloatingActionButton) mMainView.findViewById(R.id.fab);
//        fabSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                smallRecycleView.setSelected(false);
//                bigRecycleView.setSelected(false);
//                gridRecycleView.setSelected(false);
//                viewFlipper.showNext();
//
//                if(typeShowList == Small){
//                    typeShowList = Big;
//                    fabSwitch.setImageResource(R.drawable.ic_if_switch_list_grid);
//                }
//                else if(typeShowList == Big){
//                    typeShowList = Grid;
//                    fabSwitch.setImageResource(R.drawable.ic_if_switch_list_small);
//                }
//                else {
//                    typeShowList = Small;
//                    fabSwitch.setImageResource(R.drawable.ic_if_switch_list_big);
//                }
//            }
//        });
//        fabSwitch.setImageResource(R.drawable.ic_if_switch_list_big);




    }


    private  ArrayList<Users> getAllUsers(){
        final ArrayList<Users> emptyListChatTemp=new ArrayList<Users>();
        //emptyListChat=new ArrayList<Users>();

        SharedPreferences preferences =  ctx.getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
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


                    if(curr_user_id!= null) {
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

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
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

    private void createHelperWnd() {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int widthS = metrics.widthPixels;
        final int heightS = metrics.heightPixels;

        WindowManager wm = (WindowManager) ctx.getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams p = new WindowManager.LayoutParams();
        p.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        p.gravity = Gravity.RIGHT | Gravity.TOP;
        p.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        p.width = 1;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        p.format = PixelFormat.TRANSPARENT;
        final View helperWnd = new View(ctx); //View helperWnd;

        wm.addView(helperWnd, p);
        final ViewTreeObserver vto = helperWnd.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                if (heightS == helperWnd.getHeight()) {
                   // isFullScreen = true;
                } else {
                   // isFullScreen = false;
                }
            }
        });

    }
}
