package io.emaster.smashretrochat.login;

/**
 * Created by elezermaster on 24/08/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;
import okhttp3.OkHttpClient;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.activity.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText email,password,name;
    private EditText emailUp,passwordUp,nameUp;
    private Button signin, signup;
    FirebaseUser user;
    public static String LoggedIn_User_Email;
    public static String LoggedIn_User_Name;
    public static FirebaseDatabase mDatabase;

    DatabaseReference userRef;
    DatabaseReference mRootRef;
    DatabaseReference storeUserDefaultDR;
    DatabaseReference usersReference;
    /*
    Login screen prop
     */
    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;
    //LinearLayout llsignin,llsignup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        OneSignal.startInit(this).init();

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


        userRef =  FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance(); // important Call

        //signin = (Button)findViewById(R.id.signin);
        //signup = (Button)findViewById(R.id.signup);


        /////////////////////////////////////////////////////
        llSignin = (LinearLayout) findViewById(R.id.llSignin);
        //llSignup = (LinearLayout) findViewById(R.id.llSignup);
        llSignin.setOnClickListener(this);
        //LinearLayout singnin =(LinearLayout)findViewById(R.id.signin);
        llSignup =(LinearLayout)findViewById(R.id.llSignup);
        llSignup.setOnClickListener(this);
        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup= (Button) findViewById(R.id.btnSignup);
        btnSignin= (Button) findViewById(R.id.btnSignin);


        //llSignin = (LinearLayout) findViewById(R.id.llSignin);
        //////////////////////////////////////////////////////

        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etPassword);
        name = (EditText)findViewById(R.id.etName);
        emailUp = (EditText)findViewById(R.id.etEmailUp);
        passwordUp = (EditText)findViewById(R.id.etPasswordUp);
        nameUp = (EditText)findViewById(R.id.etNameUp);
        //Check if User is Already LoggedIn
        if(mAuth.getCurrentUser() != null)
        {
            //User NOT logged In
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        user = mAuth.getCurrentUser();
        Log.d("LOGGED", "user: " + user);


        //Setting the tags for Current User.
        if (user != null) {
            LoggedIn_User_Email =user.getEmail();

//            String online_user_id = mAuth.getCurrentUser().getUid();
//
//            usersReference = FirebaseDatabase.getInstance().getReference()
//                    .child("Users").child(online_user_id);
        }
        OneSignal.sendTag("User_ID", LoggedIn_User_Email);








        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = true;
                showSigninForm();
            }
        });
        showSigninForm();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
                if(isSigninScreen) {
                    btnSignup.startAnimation(clockwise);
                }else{
                    String getemail = emailUp.getText().toString().trim();
                    String getepassword = passwordUp.getText().toString().trim();
                    String getname = nameUp.getText().toString().trim();
                    callsignup(getemail,getepassword, getname);
                }
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggedIn_User_Email = email.getText().toString().trim();
                String getepassword = password.getText().toString().trim();
                callsignin(LoggedIn_User_Email,getepassword);

                LoggedIn_User_Name =LoggedIn_User_Email.substring(0, LoggedIn_User_Email.indexOf("@"));
                       // (name.getText().toString().equals("") || LoggedIn_User_Name==null)?
                       //         LoggedIn_User_Email.substring(0, LoggedIn_User_Email.indexOf("@")):
                       //         name.getText().toString();

            }
        });



    }

    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }

    //Create Account
    private void callsignup(String email, String password, final String name) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Signed up Failed", Toast.LENGTH_SHORT).show();
                            Log.e("LOGUP", "onComplete: Failed=" + task.getException().getMessage());
                        }
                        else
                        {
                            //userProfile();


                            final FirebaseUser user = mAuth.getCurrentUser();
                            String UserIDEmail=user.getEmail().replace("@","").replace(".","");
                            final String UserID = user.getUid();

                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            //store here user details
                            mRootRef = FirebaseDatabase.getInstance().getReference();
                            storeUserDefaultDR= mRootRef.child("Users").child(UserID);
                            storeUserDefaultDR.child("online").setValue("true");
                            storeUserDefaultDR.child("device_token").setValue(deviceToken);
                            storeUserDefaultDR.child("user_name").setValue(name);
                            storeUserDefaultDR.child("user_email").setValue(user.getEmail());
                            storeUserDefaultDR.child("user_id_email").setValue(UserID);
                            storeUserDefaultDR.child("user_image_url").setValue("default_profile");
                            storeUserDefaultDR.child("user_thumb_image").setValue("default_image");
                            storeUserDefaultDR.child("user_status").setValue("I'am using this cool app");
                            storeUserDefaultDR.child("user_lat").setValue("Null");
                            storeUserDefaultDR.child("user_lng").setValue("Null");
                            storeUserDefaultDR.child("user_place").setValue("Null");
                            storeUserDefaultDR.child("user_area").setValue("Null");
                            storeUserDefaultDR.child("user_sex").setValue("Null");
                            storeUserDefaultDR.child("user_age").setValue("Null");
                            storeUserDefaultDR.child("user_height").setValue("Null");
                            storeUserDefaultDR.child("user_weight").setValue("Null");
                            storeUserDefaultDR.child("user_date_birth").setValue("Null");
                            storeUserDefaultDR.child("user_marriage_status").setValue("Null");
                            storeUserDefaultDR.child("user_date_created").setValue(getCurrentDate());
                            storeUserDefaultDR.child("user_last_online").setValue("Null")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("TESTING", "Sign up Successful" + task.isSuccessful());
                                        Toast.makeText(LoginActivity.this, "Account Created ", Toast.LENGTH_SHORT).show();
                                        Log.d("TESTING", "Created Account");

                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("USER_NAME", name);
                                        //editor.putString("USER_STATUS", user_status);
                                        editor.putString("USER_EMAIL", user.getEmail());
                                        editor.putString("USER_ID", UserID);
                                        //editor.putString("USER_DATE_CREATED", user_date_created);
                                        //editor.putString("USER_LAST_ONLINE", user_last_online);
                                        //editor.putString("USER_IMAGE_URL", user_image_url);
                                        //editor.putString("USER_THUMB_IMAGE", user_thumb_image);
                                        //editor.putString("USER_LAT", user_lat);
                                        //editor.putString("USER_LNG", user_lng);
                                        //editor.putString("USER_PLACE", user_place);
                                        editor.commit();

                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        finish();
                                        startActivity(i);
                                    }else{
                                        Log.d("TESTING", "Sign up Error " + task.isSuccessful());
                                        Toast.makeText(LoginActivity.this, "Account Error on creating ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });






                        }
                    }
                });
    }

    //Set UserDisplay Name
    private void userProfile()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null)
        {
            Log.d("mAuth", "getUid(): "+user.getUid());
            Log.d("mAuth", "getEmail(): "+user.getEmail());
            Log.d("mAuth", "getDisplayName(): "+user.getDisplayName());
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(nameUp.getText().toString().trim())
//                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))  // here you can set image link also.
//                    .build();
//
//            user.updateProfile(profileUpdates)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("TESTING", "User profile updated.");
//                            }
//                        }
//                    });
        }
    }


    //Now start Sign In Process
    //SignIn Process
    private void callsignin(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "sign In Successful:" + task.isSuccessful());

                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String userEmail = user.getEmail();
                            final String userId = user.getUid();
                            if (user != null) {
                                Log.d("mAuth", "getUid(): " + user.getUid());
                                Log.d("mAuth", "getEmail(): " + user.getEmail());
                                Log.d("mAuth", "getDisplayName(): " + user.getDisplayName());

                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                userRef.child(userId).child("device_token").setValue(deviceToken)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_USER_PREF", Context.MODE_PRIVATE);
                                                final SharedPreferences.Editor editor = preferences.edit();
                                                //editor.putString("USER_NAME", name);
                                                //editor.putString("USER_STATUS", user_status);
                                                editor.putString("USER_EMAIL", userEmail);
                                                editor.putString("USER_ID", userId);
                                                //editor.putString("USER_DATE_CREATED", user_date_created);
                                                //editor.putString("USER_LAST_ONLINE", user_last_online);
                                                //editor.putString("USER_IMAGE_URL", user_image_url);
                                                //editor.putString("USER_THUMB_IMAGE", user_thumb_image);
                                                //editor.putString("USER_LAT", user_lat);
                                                //editor.putString("USER_LNG", user_lng);
                                                //editor.putString("USER_PLACE", user_place);
                                                editor.commit();

                                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                finish();
                                                startActivity(i);
                                            }
                                        });
                            }



                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                        }
                        if (!task.isSuccessful()) {
                            Log.e("LOGIN", "onComplete: Failed=" + task.getException().getMessage());
                            Log.w("TESTING", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llSignin || v.getId() ==R.id.llSignup){
            // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }
    }

    public String getCurrentDate(){
        Date date = new Date();
        Date newDate = new Date(date.getTime());// + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = dt.format(newDate);
        return  stringdate;
    }

    public String getCurrentDateTime(){
        Date date = new Date();
        Date newDate = new Date(date.getTime());// + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String stringdate = dt.format(newDate);
        return  stringdate;
    }
}

