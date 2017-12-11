//package io.emaster.smashretrochat.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.NetworkPolicy;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//import br.liveo.navigationliveo.NavigationLiveo;
//import emaster.festinflorit.FireChatHelper.PicassoClient;
//import emaster.festinflorit.R;
//import emaster.festinflorit.model.UserNearby;
//import emaster.festinflorit.ui.activity.ChatConversationActivity;
//import emaster.festinflorit.ui.activity.UserDetailsActivity;
//import emaster.festinflorit.ui.fragment.ffUserDetailsFragment;
//import io.emaster.smashretrochat.R;
//import io.emaster.smashretrochat.activity.ProfileActivity;
//import io.emaster.smashretrochat.model.Users;
//
///**
// * Created by elezermaster on 04/09/16.
// */
//public class MyAdapter extends RecyclerView.Adapter<MyHolder>{
//
//    Context context;
//    ArrayList<Users> users;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference myRef;
//
//    public MyAdapter(Context context, ArrayList<Users> users) {
//        this.context = context;
//        this.users = users;
//
//    }
//
//    @Override
//    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_profile, parent, false);
//        MyHolder myHolder = new MyHolder(view);
//        return myHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final MyHolder holder, final int position) {
//        holder.nameText.setText(users.get(position).getUser_name());
//        holder.onlineText.setText(users.get(position).getUser_status());
//        //holder.placeText.setText(users.get(position).getPlace());
//        Picasso.with(context)
//                // .load(fireChatUser.getUser_image_url()) //original image
//                .load(users.get(position).getUser_thumb_image())  //thumb img
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(ContextCompat.getDrawable(context, R.drawable.headshot_7))
//                .into(holder.userImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        Picasso.with(context)
//                                // .load(fireChatUser.getUser_image_url()) //original image
//                                .load(users.get(position).getUser_thumb_image())  //thumb img
//                                .placeholder(ContextCompat.getDrawable(context,R.drawable.headshot_7))
//                                .into(holder.userImage);
//                    }
//                });
//
//
//
//
//
//       // PicassoClient.downloadImage(context, users.get(position).getUrlFoto(),holder.userImage);
//        //click image open gallery
//        holder.userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "PHOTO click :" + position, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(context, ProfileActivity.class);
//                i.putExtra("PASS_USER", users.get(position));
//                //i.putExtra("USERNAME", users.get(position).getName());
//                //i.putExtra("USERID", users.get(position).getId());
//                context.startActivity(i);
//                //openUserDetailsFragment(users.get(position).getName(), context);
//            }
//        });
//        //click row open chat
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(context, "Row click :" + position, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(context, ProfileActivity.class);
//                i.putExtra("PASS_USER", users.get(position));
//                //i.putExtra("USERNAME", users.get(position).getName());
//                //i.putExtra("USERID", users.get(position).getId());
//                context.startActivity(i);
//
//               // DatabaseReference ref = mFirebaseAdapter.getRef(position);
//               // ref.addValueEventListener(new ValueEventListener() {
//                  //  @Override
//                  //  public void onDataChange(DataSnapshot dataSnapshot) {
//
//                       // String retrieve_name = dataSnapshot.child("Name").getValue(String.class);
//                       // String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
//                       // String retrieve_url = dataSnapshot.child("Image_URL").getValue(String.class);
//
//                        //Intent intent = new Intent(context, ChatConversationActivity.class);
//                        //intent.putExtra("USERNAME", users.get(position).getName());
//                        //intent.putExtra("USERID", users.get(position).getId());
//                        //intent.putExtra("IMAGEID", users.get(position).getUrlFoto());
//                        // intent.putExtra("email", retrieve_Email);
//                        // intent.putExtra("name", retrieve_name);
//                        //context.startActivity(intent);
//                   // }
//
//                  //  @Override
//                   // public void onCancelled(DatabaseError databaseError) {
//
//                   // }
//               // });
//            }
//        });
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//}
