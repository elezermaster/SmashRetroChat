package io.emaster.smashretrochat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import io.emaster.smashretrochat.R;
import io.emaster.smashretrochat.adapter.MessageAdapter;
import io.emaster.smashretrochat.helper.GetDateTime;
import io.emaster.smashretrochat.model.Messages;

/**
 * Created by elezermaster on 11/10/2017.
 */

public class ChatRetroActivity extends AppCompatActivity{

    String messageReceiverId;
    String messageReceiverName;

    Toolbar chatToolbar;
    TextView userNameTitle;
    TextView userLastSeen;
    CircleImageView userChatProfileImage;

    DatabaseReference rootReference;

    ImageButton sendTextBtn;
    ImageButton sendImageBtn;
    EditText messageToSend;

    FirebaseAuth mAuth;
    String messageSenderId;

    RecyclerView usermessagesList;
    private List<Messages> messagesList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    MessageAdapter messageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid().toString();


        setContentView(R.layout.activity_chat);

        messageReceiverId = getIntent().getExtras().get("PASS_USER_ID").toString();
        messageReceiverName = getIntent().getExtras().get("PASS_USER_NAME").toString();

        Log.d("CHAT", messageReceiverId + "\n" +
                        messageReceiverName);

        chatToolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(chatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(messageReceiverName);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);

        userNameTitle = (TextView)findViewById(R.id.userNameTitleBar);
        userLastSeen = (TextView)findViewById(R.id.lastViewTitleBar);
        userChatProfileImage = (CircleImageView)findViewById(R.id.userImageTitleBar);

        userNameTitle.setText(messageReceiverName);



        messageAdapter = new MessageAdapter(messagesList);
        usermessagesList = (RecyclerView)findViewById(R.id.messagesList);

        layoutManager = new LinearLayoutManager(this);
        usermessagesList.setHasFixedSize(true);
        usermessagesList.setLayoutManager(layoutManager);
        usermessagesList.setAdapter(messageAdapter);

        FetchMessages();





        //final String[] online = new String[1];// = new String;
        rootReference.child("Users").child(messageReceiverName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("online").exists()) {
                   String  online = dataSnapshot.child("online").getValue().toString();
                    if(dataSnapshot.child("online").exists() && online.length()>10){
                        userLastSeen.setText( GetDateTime.getDateFromStamp(online) );
                    }
                    if(online.equals("true")){
                        userLastSeen.setText("online");
                    }else {
                        userLastSeen.setText("offline");
                    }
                }else{
                    //dataSnapshot.child("online").getValue().toString();
                }

                String userThumbImage = "";// = "https://firebasestorage.googleapis.com/v0/b/festinflorit.appspot.com/o/Profile_Images%2Fn2Ex7QlfT0cD5K1EJbfY8vqvP4Q2.jpg?alt=media&token=68fd3ad2-5b05-4c9a-aa66-ad2c87961e5a";
                if(dataSnapshot.child("user_thumb_image").exists()) {
                    userThumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                    final String finalUserThumbImage = userThumbImage;
                    Picasso.with(ChatRetroActivity.this)
                            // .load(fireChatUser.getUser_image_url()) //original image
                            .load(userThumbImage)  //thumb img
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.ic_user)
                            .into(userChatProfileImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(ChatRetroActivity.this)
                                            // .load(fireChatUser.getUser_image_url()) //original image
                                            .load(finalUserThumbImage)  //thumb img
                                            .placeholder(R.drawable.ic_user)
                                            .into(userChatProfileImage);
                                }
                            });
                }


                sendTextBtn = (ImageButton)findViewById(R.id.send_message_btn);
                sendImageBtn = (ImageButton)findViewById(R.id.send_image_btn);
                messageToSend = (EditText)findViewById(R.id.et_SendMessage);

                sendTextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SendMessage();

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void FetchMessages() {
        rootReference = FirebaseDatabase.getInstance().getReference();

        rootReference.child("Messages").child(messageSenderId).child(messageReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Messages messages = dataSnapshot.getValue(Messages.class);
                           Log.d("MESS", messages.getMessage()+"\n"+ messages.getTime());

                           messagesList.add(messages);
                           Log.d("MESS", messages.getMessage()+"\n added\n"+ messagesList.size());
                           messageAdapter.notifyDataSetChanged();


//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            Messages messages = ds.getValue(Messages.class);
//                            Log.d("MESS", messages.getMessage()+"\n"+ messages.getTime());
//                            messagesList.add(messages);
//                            //messageAdapter.notifyDataSetChanged();
//                        }
//                        messageAdapter.notifyDataSetChanged();

//                        Messages messages = dataSnapshot.getValue(Messages.class);
//                        messagesList.add(messages);
//                        Log.d("MESS", messages.getMessage()+"\n"+
//                        messages.getTime());
//                        messageAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//        rootReference.child("Messages").child(messageSenderId).child(messageReceiverId)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            Messages messages = ds.getValue(Messages.class);
//                            Log.d("MESS", messages.getMessage()+"\n"+ messages.getTime());
//                            messagesList.add(messages);
//                            //messageAdapter.notifyDataSetChanged();
//                        }
//                        messageAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    public void SendMessage(){
        String message = messageToSend.getText().toString();
        if(TextUtils.isEmpty(message)){
            Toast.makeText(ChatRetroActivity.this, "please write message", Toast.LENGTH_LONG).show();
        }else{
            Log.d("ROOT_REF", ""+rootReference);
            Log.d("SENDER", ""+messageSenderId);
            Log.d("RECEIVER", ""+messageReceiverId);
            String message_sender_ref = "Messages/"+ messageSenderId +"/"+ messageReceiverId;
            String message_receiver_ref = "Messages/"+ messageReceiverId +"/"+ messageSenderId;
            DatabaseReference user_message_key = rootReference.child("Messages").child(messageSenderId)
                    .child(messageReceiverId).push();
            String message_push_id = user_message_key.getKey();
            Map message_text_body = new HashMap();
            message_text_body.put("message", message);
            message_text_body.put("seen", false);
            message_text_body.put("type", "text");
            message_text_body.put("time", ServerValue.TIMESTAMP);
            message_text_body.put("from", messageSenderId);
            Map message_body_details = new HashMap();
            message_body_details.put(message_sender_ref+ "/"+ message_push_id, message_text_body);
            message_body_details.put(message_receiver_ref+ "/"+ message_push_id, message_text_body);
            rootReference.updateChildren(message_body_details,new DatabaseReference.CompletionListener(){
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!= null){
                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                    }
                    messageToSend.setText("");

                }
            });

        }
    }
}
