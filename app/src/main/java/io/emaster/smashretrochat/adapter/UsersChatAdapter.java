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

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.activity.ProfileActivity;
import io.emaster.smashretrochat.helper.GetDateTime;
import io.emaster.smashretrochat.model.Users;


/**
 * Created by Marcel on 11/11/2015.
 */
public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolderUsers> {

    private List<Users> mFireChatUsers;
    private Context mContext;
    private String mCurrentUserName;
    private String mCurrentUserCreatedAt;
    private String mLastMessage;

    public UsersChatAdapter(Context context, List<Users> fireChatUsers) {
        mFireChatUsers=fireChatUsers;
        mContext=context;
        Log.i("UserChatAdapter", "context:\n"+mContext);
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("UserChatAdapter", "onCreateViewHolder");
        Log.i("UserChatAdapter", "parent:\n"+parent);
        Log.i("UserChatAdapter", "viewType:\n"+viewType);
        // Inflate layout for each row
        return new ViewHolderUsers(mContext,
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolderUsers holder, int position) {
        Log.i("UserChatAdapter", "onBindViewHolder");
        Log.i("UserChatAdapter", "holder:\n"+holder);
        Log.i("UserChatAdapter", "position:\n"+position);
        final Users fireChatUser=mFireChatUsers.get(position);

        // Set avatar
        //int userAvatarId= ChatHelper.getDrawableAvatarId(fireChatUser.getAvatarId());
        //Drawable  avatarDrawable= ContextCompat.getDrawable(mContext,userAvatarId);
        final Drawable  avatarDrawable= ContextCompat.getDrawable(mContext,R.drawable.headshot_7);

        //holder.getUserPhoto().setImageDrawable(avatarDrawable);
        Picasso.with(mContext)
               // .load(fireChatUser.getUser_image_url()) //original image
                .load(fireChatUser.getUser_thumb_image())  //thumb img
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(avatarDrawable)
                .into(holder.getUserPhoto(), new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                // .load(fireChatUser.getUser_image_url()) //original image
                                .load(fireChatUser.getUser_thumb_image())  //thumb img
                                .placeholder(avatarDrawable)
                                .into(holder.getUserPhoto());
                    }
                });


        //holder.getUserPhoto().setAdjustViewBounds(true);//.setFillColor(R.color.colorPrimary);

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

        // Set presence text color
//        if(fireChatUser.getConnection().equals(ReferenceUrl.KEY_ONLINE)) {
//            // Green color
//            holder.getStatusConnection().setTextColor(Color.parseColor("#00FF00"));
//        }else {
//            // Red color
//            holder.getStatusConnection().setTextColor(Color.parseColor("#FF0000"));
//        }

    }

    @Override
    public int getItemCount() {
        return mFireChatUsers.size();
    }

    public void refill(Users users) {

        // Add each user and notify recyclerView about change
        mFireChatUsers.add(users);
        notifyDataSetChanged();
    }

    public void setNameAndCreatedAt(String userName, String createdAt) {

        // Set current user name and time account created at
        mCurrentUserName=userName;
        mCurrentUserCreatedAt=createdAt;
    }

    public void changeUser(int index, Users user) {

        // Handle change on each user and notify change
        mFireChatUsers.set(index,user);
        notifyDataSetChanged();
    }



    /* ViewHolder for RecyclerView */
    public class ViewHolderUsers extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView mUserPhoto; // User avatar
        private TextView mUserFirstName; // User first name
        private TextView mStatusConnection; // User presence
        private TextView mUserLastMessage;
        private Context mContextViewHolder;

        public ViewHolderUsers(Context context, View itemView) {
            super(itemView);
            mUserPhoto=(CircleImageView)itemView.findViewById(R.id.userPhotoProfile);
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

            return mUserPhoto;
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

            Users user= mFireChatUsers.get(position); // Get use object

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
