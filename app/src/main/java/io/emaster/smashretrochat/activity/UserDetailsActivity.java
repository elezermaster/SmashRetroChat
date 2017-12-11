package io.emaster.smashretrochat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.builder.Item;
import com.vansuita.materialabout.views.AboutView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.emaster.smashretrochat.ChatRetroOfflineApp;
import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.model.Users;
import okhttp3.OkHttpClient;

/**
 * Created by elezermaster on 01/09/2017.
 */

public class UserDetailsActivity extends AppCompatActivity {

    private static final int GALLERY_PICK =1;

    ImageView userDetailsImageLogo;
    TextView userDetailsName;
    TextView userDetailsStatus; //tvUserDetailsStatus
    TextView userDetailsEmail;
    TextView userDetailsAddress;

    DatabaseReference userDetailDR;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    StorageReference thumbStorageRef;

    DatabaseReference usersReference;
    FirebaseUser user;

    Users currentUser;

    String user_name ="";
    String user_status ="";
    String user_email ="";
    String user_id_email ="";
    String user_date_created ="";
    String user_image_url ="";
    String user_thumb_image ="";
    String user_last_online = "";
    String user_lat;
    String user_lng;
    String user_place;
    SharedPreferences preferences;
    String current_user_id;
    String before_edit_status;

    Bitmap thumb_bitmap = null;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("mAuth", "onStart()");

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ONLINE", "TRUE");
        editor.commit();

    }

    @Override
    public void onStop () {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        final TextView myTV = (TextView) switcher.findViewById(R.id.tvUserDetailsStatus);

        if (!before_edit_status.equals(myTV.getText())) {


        userDetailDR.child("user_status").setValue(myTV.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "user status saved\n"+
                                myTV.getText(), Toast.LENGTH_LONG).show();
                    }
                });
        }


        super.onStop();
    }

//    @Override
//    public void onBackPressed() {
//        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
//        TextView myTV = (TextView) switcher.findViewById(R.id.tvUserDetailsStatus);
//        Toast.makeText(getApplicationContext(),
//                myTV.getText(),
//                Toast.LENGTH_LONG).show();
//        finish();
//        return;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("mAuth", "onCreate()");

        //get user details from firebase
        mAuth = FirebaseAuth.getInstance();
        //Fetch the Display name of current User
        user = mAuth.getCurrentUser();
        current_user_id = user.getUid();
        Log.d("mAuth", "current user: "+ current_user_id);
        userDetailDR = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(current_user_id);
        userDetailDR.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_Images");


        preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        //editor.putString("NAME", ds.getValue(UserNearby.class).getName());

        userDetailDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("User","key:::: "+ dataSnapshot.child("user_name").getKey());
                //Log.d("User","ref:::" + dataSnapshot.child("user_name").getRef().toString());
                //Log.d("User","val:::"+ dataSnapshot.child("user_name").getValue().toString());

                user_name = dataSnapshot.child("user_name").getValue().toString();
                before_edit_status = user_status = dataSnapshot.child("user_status").getValue().toString();
                user_id_email = dataSnapshot.child("user_id_email").getValue().toString();
                user_email = dataSnapshot.child("user_email").getValue().toString();
                user_date_created = dataSnapshot.child("user_date_created").getValue().toString();
                user_last_online = dataSnapshot.child("user_last_online").getValue().toString();
                user_image_url = dataSnapshot.child("user_image_url").getValue().toString();
                user_thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();
                user_lat = dataSnapshot.child("user_lat").getValue().toString();
                user_lng = dataSnapshot.child("user_lng").getValue().toString();
                user_place = dataSnapshot.child("user_place").getValue().toString();

                Log.d("User","addListenerForSingleValueEvent\n user_name :"+ user_name);
                //editor.putString("USER_NAME", user_name);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        userDetailDR.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("mAuth", "current user name: "+dataSnapshot.child("user_name").getValue().toString());
//
//                user_name = dataSnapshot.child("user_name").getValue().toString();
//                user_status = dataSnapshot.child("user_status").getValue().toString();
//                Log.d("User","addValueEventListener\n user_name :"+ user_name);
//
//
//                currentUser = new Users();
//                currentUser.setUser_name(dataSnapshot.child("user_name").getValue().toString());
//                currentUser.setUser_email(dataSnapshot.child("user_email").getValue().toString());
//                currentUser.setUser_date_created(dataSnapshot.child("user_date_created").getValue().toString());
//                currentUser.setUser_last_online(dataSnapshot.child("user_last_online").getValue().toString());
//                currentUser.setUser_status(dataSnapshot.child("user_status").getValue().toString());
//                currentUser.setUser_image_url(dataSnapshot.child("user_image_url").getValue().toString());
//                currentUser.setUser_thumb_image(dataSnapshot.child("user_thumb_image").getValue().toString());
//
//
//                //String user_name = dataSnapshot.child("user_name").getValue().toString();
//                //String user_email = dataSnapshot.child("user_email").getValue().toString();
//                //dataSnapshot.child("user_id_email").getValue().toString();
//                //String user_image_url = dataSnapshot.child("user_image_url").getValue().toString();
//                //String user_thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();
//                //String user_status = dataSnapshot.child("user_status").getValue().toString();
//                dataSnapshot.child("user_lat").getValue().toString();
//                dataSnapshot.child("user_lng").getValue().toString();
//                dataSnapshot.child("user_place").getValue().toString();
//                dataSnapshot.child("user_area").getValue().toString();
//                dataSnapshot.child("user_sex").getValue().toString();
//                dataSnapshot.child("user_age").getValue().toString();
//                dataSnapshot.child("user_height").getValue().toString();
//                dataSnapshot.child("user_weight").getValue().toString();
//                dataSnapshot.child("user_date_birth").getValue().toString();
//                dataSnapshot.child("user_marriage_status").getValue().toString();
//                //dataSnapshot.child("user_date_created").getValue().toString();
//                //dataSnapshot.child("user_last_online").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                Log.d("mAuth", "canceled getting name: ");
//            }
//        });


        if (Build.VERSION.SDK_INT >= 21) {
            setContentView(R.layout.ud_activity_profile);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ud_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();
//////////////////////Upload image logo
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_PICK);

                }
            });


            userDetailsImageLogo = (ImageView) findViewById(R.id.ud_image_logo);
            userDetailsName = (TextView) findViewById(R.id.tvUserDetailsName);
            userDetailsStatus = (TextView) findViewById(R.id.tvUserDetailsStatus);
            userDetailsEmail = (TextView) findViewById(R.id.tvUserDetailsEmail);
            userDetailsAddress = (TextView) findViewById(R.id.tvUserDetailsAddress);
            //if(user_name.equals("")){
            preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
            user_name = preferences.getString("USER_NAME", null);
            user_status = preferences.getString("USER_STATUS", null);
            user_email = preferences.getString("USER_EMAIL", null);
            user_date_created = preferences.getString("USER_DATE_CREATED", null);
            user_last_online = preferences.getString("USER_LAST_ONLINE", null);
            user_image_url = preferences.getString("USER_IMAGE_URL", null);
            user_thumb_image = preferences.getString("USER_THUMB_IMAGE", null);
            user_lat= preferences.getString("USER_LAT", null);
            user_lng= preferences.getString("USER_LNG", null);
            user_place= preferences.getString("USER_PLACE", null);

            //}

            getSupportActionBar().setTitle(user_name);
            userDetailsName.setText(user_name);
            userDetailsStatus.setText(user_status);
            userDetailsEmail.setText(user_email);
            userDetailsAddress.setText(user_place);
            //set image to userDetailsImageLogo


            Picasso.with(UserDetailsActivity.this)
                    .load(user_image_url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_user)
                    .into(userDetailsImageLogo, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(UserDetailsActivity.this)
                                    .load(user_image_url)
                                    .placeholder(R.drawable.ic_user)
                                    .into(userDetailsImageLogo);
                        }
                    });

            // get your ToggleButton
            ToggleButton b = (ToggleButton) findViewById(R.id.chkState01UStatus);
            // attach an OnClickListener
            b.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
                    switcher.showNext(); //or switcher.showPrevious();
                    TextView myTV = (TextView) switcher.findViewById(R.id.tvUserDetailsStatus);
                    preferences =  getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                    user_status = preferences.getString("USER_STATUS", null);
                    myTV.setText(user_status);
                    final EditText myET = (EditText) switcher.findViewById(R.id.hiddenETUserDetailsStatus);
                    myET.setText(user_status);
                    myET.setFocusable(true);
                    myET.setSelection(((EditText) myET).getText().length());
                    myET.append("");


                    preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                    //final SharedPreferences.Editor editor = preferences.edit();

                    String edited_user_status = "";// user_status;
                    myET.addTextChangedListener(new TextWatcher() {
                        SharedPreferences.Editor myeditor = preferences.edit();
                        @Override
                        public void afterTextChanged(Editable s) {
                            myeditor.commit();
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                            //myET.setText(user_status);
                             myeditor = preferences.edit();
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            //if(s.length() != 0)
                            //    Field2.setText("");
                            if(!s.toString().equals(user_status)){
                                myeditor.putString("USER_STATUS", s.toString());

                                //edited_user_status = s.toString();
//                                Toast.makeText(getApplicationContext(), "changed\n" +
//                                        s.toString() + "\n"+
//                                        "", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    if(!user_status.equals(user_status)){
//                        Toast.makeText(getApplicationContext(), "status changed\n" +
//                                edited_user_status + "\n"+
//                                "", Toast.LENGTH_LONG).show();
                    }


                }
            });

        }else {

        //add in gradle
        //compile 'com.github.jrvansuita:MaterialAbout:+'
        AboutView aboutBuilder = AboutBuilder.with(this)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setName(user_name)
                .setSubTitle(user_email)
                .setBrief(user_status)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGooglePlayStoreLink("8002078663318221363")
                .addGitHubLink("user")
                .addFacebookLink("user")
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

        addContentView(aboutBuilder,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

//        View lastLinkView = aboutBuilder.findItem(builder.getLastLink());
//        View lastActionView = aboutBuilder.findItem(builder.getLastAction());
//
//        List<Item> actions = aboutBuilder.getActions();
//        List<Item> links = aboutBuilder.getActions();

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            Log.d("imageUri", "imageUri:"+imageUri);
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }else {
            Toast.makeText(getApplicationContext(), "error on result for image uri", Toast.LENGTH_LONG).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //set loading bar


                Uri resultUri = result.getUri();

                File thumb_filepath = new File(resultUri.getPath());
                try{
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                thumbStorageRef = FirebaseStorage.getInstance().getReference().child("user_thumb_image");


                Log.d("resultUri", "resultUri:"+resultUri);
                StorageReference filePath = storageReference.child(current_user_id+".jpg");
                final StorageReference filePathThumb = storageReference.child(current_user_id+".jpg");


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "image profile added successfully", Toast.LENGTH_SHORT).show();
                            final String downloadUri = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = filePathThumb.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task_thumb) {
                                    String thumb_download_url = task_thumb.getResult().getDownloadUrl().toString();
                                    if(task_thumb.isSuccessful()){
                                        Map update_user_data = new HashMap();
                                        update_user_data.put("user_image_url", downloadUri);
                                        update_user_data.put("user_thumb_image", thumb_download_url);

                                        userDetailDR.updateChildren(update_user_data)
                                                //child("user_image_url").setValue(downloadUri)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(), "image url saved\n"+ downloadUri, Toast.LENGTH_LONG).show();
                                                        //loading bar dissmiss

                                                    }
                                                });
                                    }
                                }
                            });

//                            userDetailDR.child("user_image_url").setValue(downloadUri)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(getApplicationContext(), "image url saved\n"+ downloadUri, Toast.LENGTH_LONG).show();
//                                }
//                            });

                        }else{
                            Toast.makeText(getApplicationContext(), "error on uploading image profile", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

//    public void tvUserStatusClicked() {
//        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
//        switcher.showNext(); //or switcher.showPrevious();
//        TextView myTV = (TextView) switcher.findViewById(R.id.tvUserDetailsStatus);
//        myTV.setText("value");
//    }


    @Override
    public void onResume()
    {
        super.onResume();

        ChatRetroOfflineApp myApp = (ChatRetroOfflineApp)this.getApplication();
        if (myApp.wasInBackground)
        {
            //Do specific came-here-from-background code
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
    public void onPause()
    {
        super.onPause();
        ((ChatRetroOfflineApp)this.getApplication()).startActivityTransitionTimer();
    }



}
