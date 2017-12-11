package io.emaster.smashretrochat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.emaster.smashretrochat.ChatRetroOfflineApp;
import okhttp3.OkHttpClient;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.adapter.ViewPagerAdapter;
import io.emaster.smashretrochat.fragment.OneFragment;
import io.emaster.smashretrochat.fragment.ThreeFragment;
import io.emaster.smashretrochat.fragment.TwoFragment;
import io.emaster.smashretrochat.login.LoginActivity;
import io.emaster.smashretrochat.model.Users;

import static io.emaster.smashretrochat.adapter.AAdapter.TypeShow.Big;
import static io.emaster.smashretrochat.adapter.AAdapter.TypeShow.Grid;
import static io.emaster.smashretrochat.adapter.AAdapter.TypeShow.Small;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    TextView username;
    TextView useremail;
    ImageView userimage;
    static String LoggedIn_User_Email;
    static String LoggedIn_User_Name;
    static String LoggedIn_User_Img;
    public static int Device_Width;
    DatabaseReference getUserInfoRef;
    DatabaseReference usersReference;
    FirebaseUser user;
    static Users currentUser;

    TabLayout tabLayout;
    ViewPager viewPager;

    String userId;

    SharedPreferences preferences;
    FloatingActionButton fabSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get user info
        mAuth = FirebaseAuth.getInstance(); // important Call

        //Fetch the Display name of current User
        user = mAuth.getCurrentUser();
        Log.d("LOGGED", "FirebaseUser: " + user);

        if(user!= null){
           userId = user.getUid().toString();

            getUserInfoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        }


        //check shared pref if not exists
        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        String user_name = preferences.getString("USER_NAME", null);
        //String user_status = preferences.getString("USER_STATUS", null);
        //String user_email = preferences.getString("USER_EMAIL", null);
        //String user_date_created = preferences.getString("USER_DATE_CREATED", null);
        if(user_name == null && getUserInfoRef!=null) {
            getUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = new Users();
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
                    String user_online = dataSnapshot.child("online").getValue().toString();


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
                    currentUser.setUser_online(user_online);//true false timestamp

                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("USER_NAME", user_name);
                    editor.putString("USER_STATUS", user_status);
                    //editor.putString("USER_ID", user_id_email);
                    //editor.putString("USER_EMAIL", user_email);
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

        /*
        TODO: Handle UI Navigation Components
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*
         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_camara);

        // set new title to the MenuItem
        nav_camara.setTitle("NewTitleForCamera");

        //set icon
        nav_camara.setIcon(R.drawable.ic_nav_input_berita);

        // do the same for other MenuItems
        MenuItem nav_gallery = menu.findItem(R.id.nav_gallery);
        nav_gallery.setTitle("NewTitleForGallery");

        // add NavigationItemSelectedListener to check the navigation clicks
        navigationView.setNavigationItemSelectedListener(this);
         */

        /*
        TODO: Handle User data from firebase
         */
        if (LoginActivity.mDatabase == null) {
            LoginActivity.mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);

        }


        //Again check if the user is Already Logged in or Not
        user = mAuth.getCurrentUser();
        if(user == null)
        {
            //User NOT logged In
            //this.finish();
            //startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            Menu menu = navigationView.getMenu();
            // find MenuItem you want to change
            MenuItem nav_camara = menu.findItem(R.id.nav_exit);
            // set new title to the MenuItem
            nav_camara.setTitle("ENTER");
            //set icon
            nav_camara.setIcon(R.drawable.ic_power_settings_red);


            //TODO: set button on toolbar to login


        }
        else if (user != null) {
            LoggedIn_User_Name = user.getDisplayName();
            LoggedIn_User_Email =user.getEmail();


            Log.d("LOGGED", "user: " + user);


            //Setting the tags for Current User.

//            String online_user_id = mAuth.getCurrentUser().getUid();
//
//            usersReference = FirebaseDatabase.getInstance().getReference()
//                        .child("Users").child(online_user_id);
//            usersReference.child("online").setValue(true);



            Log.d("MAIN", "Welcome, " + LoginActivity.LoggedIn_User_Name);

            username = (TextView)navigationView.getHeaderView(0).findViewById(R.id.curr_user_name);
            useremail = (TextView)navigationView.getHeaderView(0).findViewById(R.id.curr_user_email);
            userimage =  (ImageView)navigationView.getHeaderView(0).findViewById(R.id.curr_user_image);

            /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            username.setText(LoggedIn_User_Name);
            useremail.setText(LoggedIn_User_Email);

            preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
            //user_name = preferences.getString("USER_NAME", null);
            //user_status = preferences.getString("USER_STATUS", null);
            //user_email = preferences.getString("USER_EMAIL", null);
            //user_date_created = preferences.getString("USER_DATE_CREATED", null);
            //user_last_online = preferences.getString("USER_LAST_ONLINE", null);
            String user_image_url = preferences.getString("USER_IMAGE_URL", null);
            if(user_image_url != null) {
                if (!user_image_url.equals("")) {
                    Picasso.with(MainActivity.this).load(user_image_url).into(userimage);
                }
            }
        }

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Device_Width = metrics.widthPixels;


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        fabSwitch = (FloatingActionButton) findViewById(R.id.fab);
        fabSwitch.setImageResource(R.drawable.ic_if_switch_list_big);
        fabSwitch.show();


    }

    @Override
    protected void onStart() {
        super.onStart();

//        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("USER_ONLINE", "TRUE");
//        editor.commit();
//
//        //user = mAuth.getCurrentUser();
//        if (user != null) {
//            //LoggedIn_User_Name = user.getDisplayName();
//            //LoggedIn_User_Email = user.getEmail();
//
//
//            Log.d("LOGGED ONSTART", "user: " + user);
//
//
//            //Setting the tags for Current User.
//
//            String online_user_id = mAuth.getCurrentUser().getUid();
//
//            usersReference = FirebaseDatabase.getInstance().getReference()
//                    .child("Users").child(online_user_id);
//            usersReference.child("online").setValue(true);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("USER_ONLINE", "FALSE");
//        editor.commit();
//        editor.apply();
//
//        usersReference.child("online").setValue(false);




        if (user != null) {
            Log.d("LOGGED ONSTOP", "user: " + user);

            getUserInfoRef.child("online").setValue(ServerValue.TIMESTAMP);


//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
//                    String is_user_online = preferences.getString("USER_ONLINE", null);
//                    if(is_user_online.equals("FALSE")){
//
//                        usersReference.child("online").setValue(false);
//                        //SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
//                        //SharedPreferences.Editor editor = preferences.edit();
//                        //editor.putString("USER_ONLINE", "FALSE");
//                        //editor.commit();
//                    }
//
//                }
//            }, 20000);

            //String online_user_id = mAuth.getCurrentUser().getUid();
            //usersReference = FirebaseDatabase.getInstance().getReference()
            //        .child("Users").child(online_user_id);
            //usersReference.child("online").setValue(false);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new TwoFragment(), "All");
        adapter.addFragment(new OneFragment(), "Friends");
        adapter.addFragment(new ThreeFragment(), "Requests");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if(user!= null) { //if logged in
            if (id == R.id.nav_people) {//1
                startActivity(new Intent(getApplicationContext(), AllUsersActivity.class));
            } else if (id == R.id.nav_messages) {//2
                sendNotification();
            } else if (id == R.id.nav_chats) {//3
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            } else if (id == R.id.nav_visitors) {//4
                startActivity(new Intent(getApplicationContext(), UploadInfoActivity.class));
            } else if (id == R.id.nav_likes) {//5
                //startActivity(new Intent(getApplicationContext(),ShowAllUsers.class));
                startActivity(new Intent(getApplicationContext(), ShowDataActivity.class));
            } else if (id == R.id.nav_gallery) {//6

            } else if (id == R.id.nav_tools) {//7
                startActivity(new Intent(getApplicationContext(), UserDetailsActivity.class));
            } else if (id == R.id.nav_exit) {

                if (user != null) {
                    getUserInfoRef.child("online").setValue(ServerValue.TIMESTAMP);
                    //usersReference.child("online").setValue(ServerValue.TIMESTAMP);
                }else {

                }

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();

                mAuth.signOut();
                finish();
                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }else {
            if (id == R.id.nav_exit) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }

        /*
         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_camara);

        // set new title to the MenuItem
        nav_camara.setTitle("NewTitleForCamera");

        // do the same for other MenuItems
        MenuItem nav_gallery = menu.findItem(R.id.nav_gallery);
        nav_gallery.setTitle("NewTitleForGallery");

        // add NavigationItemSelectedListener to check the navigation clicks
        navigationView.setNavigationItemSelectedListener(this);
         */


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendNotification(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    if (LoginActivity.LoggedIn_User_Email.equals("dudu@gmail.com")) {
                        send_email = "kukis@gmail.com";
                    } else {
                        send_email = "dudu@gmail.com";
                    }

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
                                + "\"contents\": {\"en\": \"English Message\"}"
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


    @Override
    public void onResume()
    {
        super.onResume();

        ChatRetroOfflineApp myApp = (ChatRetroOfflineApp)this.getApplication();
        if (myApp.wasInBackground)
        {
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
        }

        myApp.stopActivityTransitionTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ChatRetroOfflineApp)this.getApplication()).startActivityTransitionTimer();
    }

    public FloatingActionButton getFab(){
        return fabSwitch;
    }


    public void setFabClickListener(View.OnClickListener listener) { fabSwitch.setOnClickListener(listener); }


}
