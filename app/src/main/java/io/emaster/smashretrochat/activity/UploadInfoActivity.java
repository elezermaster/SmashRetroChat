package io.emaster.smashretrochat.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.glidebitmappool.GlideBitmapFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.adapter.CustomGridViewAdapter;
import io.emaster.smashretrochat.adapter.CustomPagerAdapter;
import io.emaster.smashretrochat.model.ItemImage;
import io.emaster.smashretrochat.model.Users;

import com.cleveroad.splittransformation.SquareViewPagerIndicator;
import com.cleveroad.splittransformation.TransformationAdapterWrapper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Administrator on 16-03-2017.
 */

public class UploadInfoActivity extends AppCompatActivity {

    Button select_image,upload_button;
    ImageView user_image;
    TextView title;
    EditText title_image;

    private ProgressDialog progressDialog;
    private Firebase mRoofRef;
    private StorageReference mStorage;
    Toolbar mToolbar;
    FloatingActionButton fab_upload;

    GridView gridView;
    static ArrayList<ItemImage> gridArray = new ArrayList<ItemImage>();
    static ArrayList<String> urls = new ArrayList<String>();
    CustomGridViewAdapter customGridAdapter;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    TransformationAdapterWrapper wrapper;
    SquareViewPagerIndicator indicator;

    DatabaseReference userDetailDR;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    StorageReference thumbStorageRef;

    StorageReference galleryStorReference;
    DatabaseReference galleryDRef;

    DatabaseReference usersReference;
    FirebaseUser user;
    String current_user_id;
    Uri imgUri;
    Uri downloadUri;

    public static final String FB_STORAGE_PATH = "gallery/";
    public static final String FB_DATABASE_PATH = "gallery";
    public static final int REQUEST_CODE = 1234;

    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upload_layout);

        final Context ctx= getApplicationContext();

        Firebase.setAndroidContext(this);
        //get user details from firebase
        mAuth = FirebaseAuth.getInstance();
        //Fetch the Display name of current User
        user = mAuth.getCurrentUser();
        current_user_id = user.getUid();
        userDetailDR = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        userDetailDR.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_Images").child(current_user_id);

        urls = getUrls();

        galleryStorReference = FirebaseStorage.getInstance().getReference().child("User_Gallery");
        galleryDRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


        mToolbar = (Toolbar)findViewById(R.id.toolbar_gallery);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Gallery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab_upload = (FloatingActionButton) findViewById(R.id.fab_upload);


        Log.d("mAuth", "current user: "+ current_user_id);




        //upload_button = (Button) findViewById(R.id.upload_bttn) ;
        //user_image = (ImageView)findViewById(R.id.user_image);
        //title_image = (EditText)findViewById(R.id.etTitle);

        fab_upload.setTag("callgallery");
        fab_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFabToOpenGallery(view);
            }
        });


        /*

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        Date createdDate = new java.util.Date();
                                        createdDate = object.getCreatedAt();
                                        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String stringDate = formatter.format(createdDate);
                                        gridArray.add(new ItemImage(bitmap, stringDate));

                                        gridView = (GridView) findViewById(R.id.gl_content_user_feed);
                                        mViewPager = (ViewPager) findViewById(R.id.pager);
                                        indicator = (SquareViewPagerIndicator) findViewById(R.id.indicator);
                                        //springIndicator = (SpringIndicator)findViewById(R.id.indicator2);
                                        customGridAdapter = new CustomGridViewAdapter(
                                                getApplicationContext(),
                                                R.layout.cell_grid,
                                                gridArray);
                                        gridView.setAdapter(customGridAdapter);
                                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                mCustomPagerAdapter = new CustomPagerAdapter(getApplicationContext(), gridArray);
                                                wrapper = TransformationAdapterWrapper
                                                        .wrap(getApplicationContext(), mCustomPagerAdapter)
                                                        // rows x column = total number of pieces. Larger number of pieces impacts on performance.
                                                        .rows(10)
                                                        .columns(7)
                                                // Maximum size of spacing between pieces.
                                                //.piecesSpacing(0.5f)
                                                // Translation for splited pieces.
                                                //.translationX(0.1f)
                                                //.translationY(0.1f)
                                                // Add top margin for view. Preffer this method instead of setting margin to your view
                                                // because transformer will split empty space into pieces too.

                                                .marginTop(getResources().getDimensionPixelSize(R.dimen.margin_top))
                                                // scale factor for generated bitmaps. Use this if you are facing any OOM issues.
                                                .bitmapScale(1f)
                                                // If you're using complex views with dynamicaly changed content (like edit texts, lists, etc)
                                                // you should provide your own complex view detector that will return true for such complex views.
                                                // Every time user swipes pager, transformer will regenerate and split bitmap for view (at the start of swipe gesture)
                                                // so make sure detector returns true only if view is a complex one.
                                                //.complexViewDetector(...)
                                                // You can set your own factory that produces bitmap transformers. Default implementation: splitting view into pieces
                                                //.bitmapTransformerFactory(...)
                                                .build();
                                                //manager = new PagerManager();
                                                //manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());


                                                //modelPagerAdapter = new ModelPagerAdapter(getSupportFragmentManager(), getModelPagerManager());
                                                mViewPager.setVisibility(View.VISIBLE);
                                                mViewPager.setAdapter(wrapper);
                                                mViewPager.setCurrentItem(i);
                                                mViewPager.setPageTransformer(false, wrapper);
                                                indicator.initializeWith(mViewPager);
                                                //springIndicator.setViewPager(mViewPager);
                                                //mViewPager.fixScrollSpeed();
                                                //springIndicator.setViewPager(mViewPager);

                                                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cast_ic_notification_disconnect));
                                                fab.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        mViewPager.setVisibility(View.GONE);
                                                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_cast_on_2_light));
                                                    }
                                                });
                                            }

                                        });


         */





        //select_image = (Button)findViewById(R.id.select_image);
        //upload_button = (Button)findViewById(R.id.upload_bttn);
        //user_image = (ImageView) findViewById(R.id.user_image);
        //title = (TextView) findViewById(R.id.etTitle);

        //Initialize the Progress Bar
        progressDialog = new ProgressDialog(UploadInfoActivity.this);

        //gridArray = new ArrayList<>();
        //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        //Date createdDate = new java.util.Date();
        //createdDate = object.getCreatedAt();
        //Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String stringDate = formatter.format(createdDate);
        //gridArray.add(new ItemImage(bitmap, "-- --")); //new ItemImage(bitmap, stringDate)

//        StorageReference ref = FirebaseStorage.getInstance().getReference("users/" + MainActivity.mainUser.getShopId() + "/gallery/" + keys.get(position));
//
//        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                //This line of code is responsible for loading the Url Image
//                Glide.with(context).load(uri).into(mImageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("UPLOAD_FAIL", "Failure: " + keys.get(position));
//            }
//        });




        userDetailDR.child("user_images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    final String url = d.getValue().toString();
                    urls.add(url);

                    //final Bitmap[] theBitmap = {null};

                    Log.d("IMG", ""+ url);
                    //Bitmap bitmap = null;// GlideBitmapFactory.decodeFile(url);//,100,100);





//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    //theBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    try {
//                        Glide.with(UploadInfoActivity.this)
//                                .asBitmap()
//                                .load(stream.toByteArray())
//                                //.error(R.drawable.bg_people)
//                               // .transform(new CircleTransform(this))
//                               // .into(imageview);
//                                .into(100,100)
//                                .get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//
//                                //image.setImageBitmap(theBitmap);
//                                //Log.d(TAG, "Image loaded");
//                                Log.d("URRL", url);
//                                String name = "";
//                                // ItemImage itemImage = new ItemImage(name, url);
//                                ItemImage itemImage = new ItemImage(theBitmap, "---");
//                                gridArray.add(itemImage);
//

//


//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... params) {
//                            Looper.prepare();
//                            try {
//                                theBitmap[0] = Glide.
//                                        with(UploadInfoActivity.this).
//                                        asBitmap().
//                                        load(url).
//
//                                        into(30,30).
//                                        get();
//                            } catch (final ExecutionException e) {
//                               // Log.e("BITMAP", e.getMessage());
//                            } catch (final InterruptedException e) {
//                               // Log.e("BITMAP", e.getMessage());
//                            }
//                            return null;
//                        }
//                        @Override
//                        protected void onPostExecute(Void dummy) {
//                            if (null != theBitmap[0]) {
//                                // The full bitmap should be available here
//                                //image.setImageBitmap(theBitmap);
//                                //Log.d(TAG, "Image loaded");
//                                Log.d("URRL", url);
//                                String name = "";
//                                // ItemImage itemImage = new ItemImage(name, url);
//                                ItemImage itemImage = new ItemImage(theBitmap[0], "---");
//                                gridArray.add(itemImage);
//
//                            };
//                        }
//                    }.execute();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //urls = getUrls();
        if(urls.size()>0){

            for(final String url: urls) {
                Picasso.with(ctx)
                        .load(url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                // loaded bitmap is here (bitmap)

                                ItemImage itemImage = new ItemImage(bitmap, "---", url);
                                Log.d("IMG", "itemImage:" + itemImage);
                                Log.d("IMG", "bitmap:" + bitmap);
                                Log.d("IMG", "url:" + url);
                                Log.d("IMG", "from:" + from);
                                gridArray.add(itemImage);
                                gridArray.notify();
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Log.d("IMG", "" + errorDrawable);
                                Log.d("IMG", "onBitmapFailed");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                Log.d("IMG", "onPrepareLoad");
                                Log.d("IMG", "" + placeHolderDrawable);
                            }
                        });
            }
        }else {
            Log.d("IMG", "urls 0" );
        }




        gridView = (GridView) findViewById(R.id.gl_content_user_feed);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (SquareViewPagerIndicator) findViewById(R.id.indicator);
        //springIndicator = (SpringIndicator)findViewById(R.id.indicator2);


        if(gridArray.size() >0) {
            customGridAdapter = new CustomGridViewAdapter(
                    ctx,
                    R.layout.cell_grid,
                    gridArray);
            customGridAdapter.notifyDataSetChanged();
        }else {
            Log.d("GRID", "size 0");
        }

        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCustomPagerAdapter = new CustomPagerAdapter(ctx, gridArray);
                wrapper = TransformationAdapterWrapper
                        .wrap(ctx, mCustomPagerAdapter)
                        // rows x column = total number of pieces. Larger number of pieces impacts on performance.
                        .rows(10)
                        .columns(7)
                        // Maximum size of spacing between pieces.
                        //.piecesSpacing(0.5f)
                        // Translation for splited pieces.
                        //.translationX(0.1f)
                        //.translationY(0.1f)
                        // Add top margin for view. Preffer this method instead of setting margin to your view
                        // because transformer will split empty space into pieces too.

                        .marginTop(getResources().getDimensionPixelSize(R.dimen.margin_top))
                        // scale factor for generated bitmaps. Use this if you are facing any OOM issues.
                        .bitmapScale(1f)
                        // If you're using complex views with dynamicaly changed content (like edit texts, lists, etc)
                        // you should provide your own complex view detector that will return true for such complex views.
                        // Every time user swipes pager, transformer will regenerate and split bitmap for view (at the start of swipe gesture)
                        // so make sure detector returns true only if view is a complex one.
                        //.complexViewDetector(...)
                        // You can set your own factory that produces bitmap transformers. Default implementation: splitting view into pieces
                        //.bitmapTransformerFactory(...)
                        .build();
                //manager = new PagerManager();
                //manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());


                //modelPagerAdapter = new ModelPagerAdapter(getSupportFragmentManager(), getModelPagerManager());
                mViewPager.setVisibility(View.VISIBLE);
                mViewPager.setAdapter(wrapper);
                mViewPager.setCurrentItem(i);
                mViewPager.setPageTransformer(false, wrapper);
                indicator.initializeWith(mViewPager);
                gridView.setVisibility(View.GONE);
                fab_upload.setTag("callpager");
                Log.d("FAB_STATUS", "callpager");
                //springIndicator.setViewPager(mViewPager);
                //mViewPager.fixScrollSpeed();
                //springIndicator.setViewPager(mViewPager);

                fab_upload.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_menu_gallery));
                fab_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridView.setVisibility(View.VISIBLE);
                        mViewPager.setVisibility(View.GONE);

                        setFabToOpenGallery(view);
                        fab_upload.setTag("callgallery");
                        Log.d("FAB_STATUS", "callgallery");

                        fab_upload.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_file_upload_white_48px));
                        //R.drawable.ic_file_upload_white_48px

                    }

                });
            }

        });



//        //Select Image From External Storage..
//        select_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                //Check for Runtime Permission
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED)
//                {
//                    Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                    {
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
//                    }
//                }
//                else
//                {
//                    callgalary();
//                }
//            }
//        });

        //Initialize Firebase Database paths for database and Storage

        //DatabaseReference mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        //https://festinflorit.firebaseio.com/

        //mRoofRef = new Firebase("https://festinflorit.firebaseio.com/").child("User_Details").push();  // Push will create new child every time we upload data
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://festinflorit.appspot.com/");

        userDetailDR.child("user_images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    final String url = d.getValue().toString();
                    urls.add(url);

                    //final Bitmap[] theBitmap = {null};

                    Log.d("IMG", ""+ url);
                    //Bitmap bitmap = null;// GlideBitmapFactory.decodeFile(url);//,100,100);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //urls = getUrls();
        if(urls.size()>0){

            for(final String url: urls) {
                Picasso.with(ctx)
                        .load(url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                // loaded bitmap is here (bitmap)

                                ItemImage itemImage = new ItemImage(bitmap, "---", url);
                                Log.d("IMG", "itemImage:" + itemImage);
                                Log.d("IMG", "bitmap:" + bitmap);
                                Log.d("IMG", "url:" + url);
                                Log.d("IMG", "from:" + from);
                                gridArray.add(itemImage);
                                //gridArray.notify();
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Log.d("IMG", "" + errorDrawable);
                                Log.d("IMG", "onBitmapFailed");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                Log.d("IMG", "onPrepareLoad");
                                Log.d("IMG", "" + placeHolderDrawable);
                            }
                        });
            }
        }else {
            Log.d("IMG", "urls 0" );
        }


        if(gridArray.size() >0) {
            customGridAdapter = new CustomGridViewAdapter(
                    ctx,
                    R.layout.cell_grid,
                    gridArray);
            customGridAdapter.notifyDataSetChanged();
        }else {
            Log.d("GRID", "size 0");
        }



        //Click on Upload Button Title will upload to Database
//        upload_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final String mName = title.getText().toString().trim();
//
//
//                if(mName.isEmpty())
//                {
//                    Toast.makeText(ctx, "Fill all Field", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //Firebase childRef_name = mRoofRef.child("Image_Title");
//                //childRef_name.setValue(mName);
//
//                String key = userDetailDR.child("user_images").push().getKey();
//                Log.d("KEY", key);
//                //mFirebaseDatabase.child("product-images").child(yourKey).child(imageKey).setValue(downloadUrl.toString());
//                userDetailDR.child("user_images").child(key).setValue(downloadUri.toString());
//
//                Toast.makeText(ctx, "Updated Info", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }

    void setFabToOpenGallery(View view){
        //Check for Runtime Permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
            }
        }
        else if(view.getTag().toString().equals("callgallery"))
        {
            callgalary();
        }
    }

    //If Access Granted gallery Will open
    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    private ArrayList<String> getUrls(){
        DatabaseReference imagesDR = userDetailDR.child("user_images");

        final ArrayList<String> emptyUrls = new ArrayList<String>();


        imagesDR.keepSynced(true);
        imagesDR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    final String url = d.getValue().toString();
                    emptyUrls.add(url);

                    Log.d("IMG", ""+ url);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return emptyUrls;
    }

    public void btnBrowseClick(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE);
    }

    //After Selecting image from gallery image will directly uploaded to Firebase Database
    //and Image will Show in Image View
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            Uri mImageUri = data.getData();
            //user_image.setImageURI(mImageUri);
            showDialogBuilder(mImageUri);
            hideSoftKeyboard();

        }

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData()!= null){
            imgUri = data.getData();
            try{
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                user_image.setImageBitmap(bmp);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //hide keyboaard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //try this
        isKeyBoardShow(this);

        //and this
        //hideKeyboardwithoutPopulate(this);


    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImageToStorage(Uri mImageUri){
        StorageReference filePath = mStorage.child("User_Images").child(current_user_id).child(mImageUri.getLastPathSegment());
        Toast.makeText(getApplicationContext(), "path:"+filePath.toString(), Toast.LENGTH_LONG).show();

        progressDialog.setMessage("Uploading Image....");
        //progressDialog.show();

        filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error

//                Glide.with(UploadInfoActivity.this)
//                        .load(downloadUri)
//                        .into(user_image);

                Toast.makeText(getApplicationContext(), "Updated.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                //final String mName = title.getText().toString().trim();

//                if(mName.isEmpty())
//                {
//                    Toast.makeText(ctx, "Fill all Field", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                //Firebase childRef_name = mRoofRef.child("Image_Title");
                //childRef_name.setValue(mName);

                String key = userDetailDR.child("user_images").push().getKey();
                Log.d("KEY", key);
                //mFirebaseDatabase.child("product-images").child(yourKey).child(imageKey).setValue(downloadUrl.toString());
                userDetailDR.child("user_images").child(key).setValue(downloadUri.toString());

                Toast.makeText(getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT).show();
            }


        });



    }

    public void showDialogBuilder(final Uri imageUri){
        LinearLayout layout;
        EditText editName;
        ImageView tosendImage;

        editName = new EditText(getApplicationContext());
        tosendImage = new ImageView(getApplicationContext());
        //final EditText editMobile = new EditText(context);
        //final EditText input = new EditText(MainActivity.this);
        ///LayoutInflater inflater = LayoutInflater.from(context);
        ///View dialogview = inflater.inflate(R.layout.dialoglayout, null);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
       // builder = new AlertDialog.Builder(getApplicationContext());//,android.R.style.Theme_Material_Light_Dialog_Alert);
                //android.R.style.Theme_Material_Dialog_Alert);
        hideSoftKeyboard();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout = new LinearLayout(getApplicationContext());
        lp.setMargins(10,5,10,5);

        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setBackgroundColor(Color.WHITE);

        //add image
        tosendImage.setImageURI(imageUri);

        layout.addView(tosendImage);

        //add text
        editName.setLayoutParams(lp);
        String path = imageUri.toString();
        String filename = path.substring(path.lastIndexOf("/")+1);
        editName.setText(filename);
        editName.setGravity(Gravity.CENTER_HORIZONTAL);
        editName.setTextSize((long)32);
        editName.setTextColor(Color.RED);
        //editName.setFocusable(true);
        //editName.setHint("name");
        editName.setFocusable(false);
        editName.setFocusableInTouchMode(false);
        //editName.setEnabled(false);
        editName.setClickable(true);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.setEnabled(true);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);

            }
        });

        //editName.performClick();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            //editName.setRevealOnFocusHint(true);
        }
        layout.addView(editName);
        hideSoftKeyboard();
        Resources.Theme currTheme = getApplicationContext().getTheme();
        Log.v("TAG", "Curr Theme : "+ currTheme);



        builder.setTitle("Send Image")
                //.setTheme(R.style.AppTheme_PopupOverlay).
                .setMessage("Are you sure you want to send this image?")
                .setView(editName)
                .setView(layout)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // mAdapter.onDataSetNotifyChanged();
                       // Toast.makeText(getApplicationContext(), "pos clicked", Toast.LENGTH_SHORT).show();
                       // uploadImageToStorage(imageUri);
                        btnUploadClick(imageUri);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "nega clicked", Toast.LENGTH_SHORT).show();
                        // do nothing
                        //((ItemViewHolder) viewHolder).onItemClear(context);
                        //mAdapter.;
                        //mAdapter.notifyDataSetChanged();

                        //mAdapter.onDataSetNotify();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_edit);
        //.show();

        AlertDialog dialog;
        dialog = builder.create();
        //dialog.getWindow().getDecorView().getBackground().setColorFilter(0xffffd793, PorterDuff.Mode.LIGHTEN);//setColorFilter(new LightingColorFilter(0xFF000000, mColorPrimary));
        //LinearLayout layoutForDialogEdit = new LinearLayout(context);


        // dialog.getWindow().getCurrentFocus().bringToFront();
        //parentView.invalidate();

        //dialog.getWindow().setStatusBarColor(Color.BLUE);



        dialog.show();

        // Initially disable the button
//        Button buttonPositive;
//        buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        buttonPositive.setEnabled(true);
//        buttonPositive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });



//
//        editName.addTextChangedListener(new TextChangedListener<EditText>(editName) {
//            @Override
//            public void onTextChanged(EditText target, Editable s) {
//                if(editName.getText().toString().equals(nameOfContact)) {
//                    buttonPositive.setEnabled(false);
//                }else {
//                    buttonPositive.setEnabled(true);
//                }
//            }
//        });
    }

    public void btnUploadClick(Uri imgUri){
        if(imgUri!= null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("uploading image");
            dialog.show();

            StorageReference filePath = mStorage.child("User_Images").child(current_user_id)
                    .child(imgUri.getLastPathSegment());

            StorageReference ref = galleryStorReference.child(current_user_id)
                    .child(current_user_id+ "_"+System.currentTimeMillis()+"."+getImageExt(imgUri));

            filePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //dialog.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "image uploaded", Toast.LENGTH_SHORT).show();


                    downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error

//                Glide.with(UploadInfoActivity.this)
//                        .load(downloadUri)
//                        .into(user_image);

                    Toast.makeText(getApplicationContext(), "Updated.", Toast.LENGTH_SHORT).show();
                    //final String mName = title.getText().toString().trim();

//                if(mName.isEmpty())
//                {
//                    Toast.makeText(ctx, "Fill all Field", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                    //Firebase childRef_name = mRoofRef.child("Image_Title");
                    //childRef_name.setValue(mName);

                    String key = userDetailDR.child("user_images").push().getKey();
                    Log.d("KEY", key);
                    //mFirebaseDatabase.child("product-images").child(yourKey).child(imageKey).setValue(downloadUrl.toString());
                    userDetailDR.child("user_images").child(key).setValue(downloadUri.toString());

                    Toast.makeText(getApplicationContext(), "Updated Info", Toast.LENGTH_SHORT).show();






                    //ItemImage itemImage = new ItemImage(title_image.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //dialog.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double process = 100* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                    dialog.setMessage("uploaded "+ (int)process +" bytes");
                    if((int)process == 100){
                        dialog.dismiss();
                    }
                }

            });
        }else {
            Toast.makeText(getApplicationContext(), "please select image", Toast.LENGTH_LONG).show();
        }
    }


    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void isKeyBoardShow(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        } else {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
        }
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }




}
