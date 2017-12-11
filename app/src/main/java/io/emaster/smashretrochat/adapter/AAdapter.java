package io.emaster.smashretrochat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.activity.ChatConversationActivity;
import io.emaster.smashretrochat.activity.ProfileActivity;
import io.emaster.smashretrochat.activity.ShowDataActivity;
import io.emaster.smashretrochat.helper.GetDateTime;
import io.emaster.smashretrochat.helper.PicassoClient;
import io.emaster.smashretrochat.model.UserNearby;
import io.emaster.smashretrochat.model.Users;

/**
 * Created by elezermaster on 03/12/2017.
 */


public class AAdapter extends RecyclerView.Adapter<AAdapter.ViewHolderUsers> {

    public enum TypeShow {
        Grid,
        Small,
        Big }

    //private AList noteList = null;
    private TypeShow typeShow;
    private List<Users> mFireChatUsers;
    private Context mContext;
    private String mCurrentUserName;
    private String mCurrentUserCreatedAt;

    public AAdapter(Context mContext,
                    ArrayList<Users> users,
                    //AdapterView.OnItemClickListener mListener,
                    TypeShow typeShow){
        this.mFireChatUsers = users;
        this.mContext= mContext;
        //this.mListener= mListener;
        this.typeShow= typeShow;
    }

    public TypeShow getAdapter(){
        return typeShow;
    }

    @Override
    public int getItemCount() {
        return mFireChatUsers.size();
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup viewGroup, int parent) {
        ///return new UsersChatAdapter.ViewHolderUsers(mContext,
        ///        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_user_profile, parent, false));
        if(typeShow == TypeShow.Grid) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_user_grid, viewGroup, false);
            return new ViewHolderUsers(viewGroup.getContext(), v, TypeShow.Grid);
        }
        else if(typeShow == TypeShow.Small) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_user_profile, viewGroup, false);
            return new ViewHolderUsers(viewGroup.getContext(),v, TypeShow.Small);
        }
        else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_user_bigger, viewGroup, false);
            return new ViewHolderUsers(viewGroup.getContext(),v, TypeShow.Big);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolderUsers holder, final int position) {
        final Users fireChatUser=mFireChatUsers.get(position);
        final Drawable avatarDrawable= ContextCompat.getDrawable(mContext,R.drawable.headshot_7);

        Picasso.with(mContext)
                .load(fireChatUser.getUser_thumb_image())
                .fit().centerCrop()
                .placeholder(avatarDrawable)
                .error(R.drawable.ic_account)
                .into(holder.getUserPhoto());


//        Picasso.with(mContext)
//                // .load(fireChatUser.getUser_image_url()) //original image
//                .load(fireChatUser.getUser_thumb_image())  //thumb img
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(avatarDrawable)
//                .into(holder.getUserPhoto(), new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        Picasso.with(mContext)
//                                // .load(fireChatUser.getUser_image_url()) //original image
//                                .load(fireChatUser.getUser_thumb_image())  //thumb img
//                                .placeholder(avatarDrawable)
//                                .into(holder.getUserPhoto());
//                    }
//                });



        if(fireChatUser.getUser_online() != null) {
            if (fireChatUser.getUser_online().equals("true")) {
                holder.setImageOnline();
            } else if(fireChatUser.getUser_online().equals("false")){
                holder.setImageOffline();
            } else{
                // Set presence statu
                String timeStamp = fireChatUser.getUser_online();
                //long last_seen = Long.parseLong(timeStamp);
                //long time =Long.valueOf(timeStamp).longValue();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
                Date time = null;
                long longTime = 0;// = time.getTime();
                try {
                    time = sdf.parse(timeStamp);
                    longTime = time.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.getStatusConnection().setText(//fireChatUser.getUser_online());
                        // GetDateTime.friendlyTimeDiff(fireChatUser.getUser_online())

                        GetDateTime.getTimeAgo(timeStamp, mContext)
                );
            }
        }else{
            holder.getStatusConnection().setText(//fireChatUser.getUser_online());
                    "offline"
            );
        }




        // Set username
        holder.getUserFirstName().setText(fireChatUser.getUser_name());
        if(fireChatUser.getUser_last_message_from_current()!=null){
            holder.getUserLastMessage().setText(fireChatUser.getUser_last_message_from_current());
        }else {
            holder.getUserLastMessage().setText(fireChatUser.getUser_status());
            holder.getUserLastMessage().setTextColor(Color.GRAY);
        }

//
//        //click image open gallery
//        holder.getUserPhoto().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "Photo click :" + position, Toast.LENGTH_SHORT).show();
//                //Intent i = new Intent(mContext, ShowDataActivity.class);//UserDetailsActivity.class);
//                //i.putExtra("USERNAME", mFireChatUsers.get(position).getUser_name());
//                //i.putExtra("USERID", mFireChatUsers.get(position).getUser_id_email());
//                //mContext.startActivity(i);
//
//                //openUserDetailsFragment(users.get(position).getName(), context);
//            }
//        });
        //click row open chat
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DatabaseReference ref = mFirebaseAdapter.getRef(position);
                // ref.addValueEventListener(new ValueEventListener() {
                //  @Override
                //  public void onDataChange(DataSnapshot dataSnapshot) {

                // String retrieve_name = dataSnapshot.child("Name").getValue(String.class);
                // String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
                // String retrieve_url = dataSnapshot.child("Image_URL").getValue(String.class);

                Toast.makeText(mContext, "Item click :" + position, Toast.LENGTH_SHORT).show();

                //int position=getLayoutPosition(); // Get row position

                Users user=mFireChatUsers.get(position); // Get use object

                // Provide current user username and time created at
                //user.setUser_name(mCurrentUserName);
                //user.setUser_status(mCurrentUserCreatedAt);

                mCurrentUserName = user.getUser_name();
                mCurrentUserCreatedAt = user.getUser_status();
                // Create a chat activity
                Intent chatIntent=new Intent(mContext, ProfileActivity.class);

                // Attach data to activity as a parcelable object
                //chatIntent.putExtra("PASS_USER",user);
                Bundle b = new Bundle();
                b.putString("PASS_USER_NAME", mCurrentUserName);
                b.putString("PASS_USER_STATUS", mCurrentUserCreatedAt);
                b.putParcelable("PASS_USER", user);
                //b.putString("key3", "value3");
                chatIntent.putExtras(b);
                //chatIntent.putExtra("PASS_USER_NAME",mCurrentUserName);

                chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start new activity
                mContext.startActivity(chatIntent);

//                Intent intent = new Intent(mContext, ChatConversationActivity.class);
//                intent.putExtra("USERNAME", mFireChatUsers.get(position).getUser_name());
//                intent.putExtra("USERID", mFireChatUsers.get(position).getUser_id_email());
//                intent.putExtra("IMAGEID", mFireChatUsers.get(position).getUser_image_url());
//                // intent.putExtra("email", retrieve_Email);
//                // intent.putExtra("name", retrieve_name);
//                mContext.startActivity(intent);
                // }

                //  @Override
                // public void onCancelled(DatabaseError databaseError) {

                // }
                // });
            }
        });
    }


//    public static class PersonViewHolder extends RecyclerView.ViewHolder {
//
//        PersonViewHolder(View itemView) {
//            super(itemView);
//
//        }
//    }

    /* ViewHolder for RecyclerView */
    public class ViewHolderUsers extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView mUserPhoto; // User avatar
        private ImageView mUserPhotoGrid; // User avatar
        private TextView mUserFirstName; // User first name
        private TextView mStatusConnection; // User presence
        private TextView mUserLastMessage;
        private Context mContextViewHolder;
        private TypeShow type;

        public ViewHolderUsers(Context context, View itemView, TypeShow type) {
            super(itemView);
            this.type = type;
            if(type == TypeShow.Grid){
                mUserPhotoGrid=(ImageView)itemView.findViewById(R.id.userPhotoProfile);
            }else {
                mUserPhoto=(CircleImageView)itemView.findViewById(R.id.userPhotoProfile);
            }

            //mUserPhoto.setFillColor(Color.BLUE);
            mUserFirstName=(TextView)itemView.findViewById(R.id.userFirstNameProfile);
            mStatusConnection=(TextView)itemView.findViewById(R.id.connectionStatus);
            mUserLastMessage =(TextView)itemView.findViewById(R.id.userLastMessage);
            mContextViewHolder=context;
            Log.i("UserChatAdapter", "context:\n"+context);
            Log.i("UserChatAdapter", "itemView:\n"+itemView);
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        public ImageView getUserPhoto() {
            if(type == TypeShow.Grid){
              return mUserPhotoGrid;
            }else {
               return mUserPhoto;
            }
        }

        public void setImageOnline(){
            mUserPhoto.setBorderWidth(2);
            mUserPhoto.setBorderColor(Color.GREEN);
        }

        public void setImageOffline(){

            mUserPhoto.setBorderWidth(1);
            mUserPhoto.setBorderColor(Color.RED);
        }

        public TextView getUserFirstName() {
            return mUserFirstName;
        }
        public TextView getStatusConnection() {
            return mStatusConnection;
        }
        public TextView getUserLastMessage() { return  mUserLastMessage;}

        @Override
        public void onClick(View view) {

            // Handle click on each row

            int position=getLayoutPosition(); // Get row position

            Users user=mFireChatUsers.get(position); // Get use object

            // Provide current user username and time created at
            //user.setUser_name(mCurrentUserName);
            //user.setUser_status(mCurrentUserCreatedAt);

            mCurrentUserName = user.getUser_name();
            mCurrentUserCreatedAt = user.getUser_status();
            // Create a chat activity
            Intent chatIntent=new Intent(mContextViewHolder, ProfileActivity.class);

            // Attach data to activity as a parcelable object
            //chatIntent.putExtra("PASS_USER",user);
            Bundle b = new Bundle();
            b.putString("PASS_USER_NAME", mCurrentUserName);
            b.putString("PASS_USER_STATUS", mCurrentUserCreatedAt);
            b.putParcelable("PASS_USER", user);
            //b.putString("key3", "value3");
            chatIntent.putExtras(b);
            //chatIntent.putExtra("PASS_USER_NAME",mCurrentUserName);

            chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Start new activity
            mContextViewHolder.startActivity(chatIntent);


            // Create a chat activity
//            Intent chatIntent=new Intent(mContextViewHolder, ChatActivity.class);
//
//            // Attach data to activity as a parcelable object
//            chatIntent.putExtra(ReferenceUrl.KEY_PASS_USERS_INFO,user);
//
//            // Start new activity
//            mContextViewHolder.startActivity(chatIntent);

        }
    }


}
