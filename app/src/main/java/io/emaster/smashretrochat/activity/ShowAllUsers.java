//package io.emaster.smashretrochat.activity;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.firebase.client.ChildEventListener;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.util.ArrayList;
//
//import io.emaster.smashretrochat.R;
//import io.emaster.smashretrochat.adapter.MyAdapter;
//import io.emaster.smashretrochat.adapter.TabsPagerAdapter;
//import io.emaster.smashretrochat.helper.ReferenceUrl;
//import io.emaster.smashretrochat.model.UserNearby;
//import io.emaster.smashretrochat.model.Users;
//
///**
// * Created by elezermaster on 28/08/2017.
// */
//
//public class ShowAllUsers extends AppCompatActivity {
//
//    public static final String TEXT_FRAGMENT = "ALL MESSAGES";
//    RecyclerView recyclerView;
//    FirebaseAuth auth;
//    MyAdapter myAdapter;
//    ArrayList<Users> users = new ArrayList<>();
//    Firebase fire = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL);
//
//    ViewPager mViewPager;
//    TabLayout mTabLayout;
//    TabsPagerAdapter mTabsPagerAdapter;
//
//    //FirebaseClient firebaseClient;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.users_nearby_layout);
//
//
//        mViewPager = (ViewPager)findViewById(R.id.show_data_view_pager);
//        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(mTabsPagerAdapter);
//        mTabLayout = (TabLayout)findViewById(R.id.show_data_tabs);
//        mTabLayout.setupWithViewPager(mViewPager);
//
//
//        //Toast.makeText(getActivity().getApplicationContext(), "user :"+ user.getProviders().toString(), Toast.LENGTH_LONG).show();
//
//        // View rootView = inflater.inflate(R.layout.users_nearby_layout, container, false);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_news);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//
//        fire.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                getUpdates(dataSnapshot);
//                Log.i("CHANGE", "onChildAdded\n" + s);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                getUpdates(dataSnapshot);
//                Log.i("CHANGE", "onChildChanged\n" + s);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
//
//    private void getUpdates(DataSnapshot dataSnapshot){
//        //users.clear();
//        for(DataSnapshot ds: dataSnapshot.getChildren()){
//            Users user = new Users();
//            user.setId(ds.getValue(UserNearby.class).getId());
//            user.setName(ds.getValue(UserNearby.class).getName());
//            user.setAge(ds.getValue(UserNearby.class).getAge());
//            user.setPlace(ds.getValue(UserNearby.class).getPlace());
//            user.setIsOnline(ds.getValue(UserNearby.class).getIsOnline());
//            user.setUrlFoto(ds.getValue(UserNearby.class).getUrlFoto());
//            user.setPlace(ds.getValue(UserNearby.class).getPlace());
//            user.setArea(ds.getValue(UserNearby.class).getArea());
//            user.setLat(ds.getValue(UserNearby.class).getLat());
//            user.setLng(ds.getValue(UserNearby.class).getLng());
//            ArrayList<String> arrayUrls = new ArrayList<String>();
//            if(ds.getValue(UserNearby.class).getImagesUrl()!=null){
//                for(String url: ds.getValue(UserNearby.class).getImagesUrl()) {
//                    Log.i("URLS","getValue: "+url);
//                    arrayUrls.add(url);
//                }
//            }
//            user.se.setImagesUrl(arrayUrls );
//            Log.i("UPDATES","user: " +user.getName());
//            users.add(user);
//        }
//        if(users.size()>0){
//            Log.i("GETUPDATES","users size: " +users.size());
//            //Log.i("GETUPDATES","context: " +context);
//            myAdapter = new MyAdapter(getApplicationContext(), users);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setAdapter(myAdapter);
//        }else {
//            Toast.makeText(getApplicationContext(), "No data users", Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
//}
