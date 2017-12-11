package io.emaster.smashretrochat.adapter;

import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.model.Messages;

/**
 * Created by elezermaster on 24/10/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> userMessagesList;

    FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> userMessagesList  ){
        this.userMessagesList = userMessagesList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;

        mAuth = FirebaseAuth.getInstance();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_user_message, parent, false);

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        String message_user_id = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);
        String fromUserId =  messages.getFrom();

        if(!message_user_id.equals(fromUserId)){
            holder.messageText.setBackgroundResource(R.drawable.message_text_background_2);
            holder.messageText.setTextColor(Color.BLACK);
            holder.messageText.setGravity(Gravity.RIGHT);
            holder.messageText.setGravity(Gravity.END);


            // Create the LayoutParams
            //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            // RelativeLayout.LayoutParams.WRAP_CONTENT,
            // RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)  holder.messageProfileImage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageProfileImage.setLayoutParams(params);

            params =  (RelativeLayout.LayoutParams)  holder.messageText.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, R.id.message_profile_image);
            params.addRule(RelativeLayout.START_OF, R.id.message_profile_image);
           // params.setMargins(0, 15, 0, 15);
            holder.messageText.setLayoutParams(params);
            ((ViewGroup.MarginLayoutParams)  holder.messageText.getLayoutParams()).leftMargin =15;
            ((ViewGroup.MarginLayoutParams)  holder.messageText.getLayoutParams()).rightMargin =15;
            ((ViewGroup.MarginLayoutParams)  holder.messageText.getLayoutParams()).topMargin =15;
            ((ViewGroup.MarginLayoutParams)  holder.messageText.getLayoutParams()).bottomMargin =15;


            //holder.messageProfileImage.setForegroundGravity(Gravity.RIGHT);
            //holder.messageProfileImage.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            //holder.messageProfileImage.setLayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, );
        }else {

        }

        holder.messageText.setText(messages.getMessage());
        //holder.messageProfileImage.setImageDrawable();
//        Picasso.with(context)
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
////                        Picasso.with(mContext)
////                                // .load(fireChatUser.getUser_image_url()) //original image
////                                .load(fireChatUser.getUser_thumb_image())  //thumb img
////                                .placeholder(avatarDrawable)
////                                .into(holder.getUserPhoto());
//                    }
//                });
    }

    @Override
    public int getItemCount() {
       return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageText;
        public CircleImageView messageProfileImage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView)itemView.findViewById(R.id.message_text_round);
            messageProfileImage =(CircleImageView)itemView.findViewById(R.id.message_profile_image);
        }
    }
}
