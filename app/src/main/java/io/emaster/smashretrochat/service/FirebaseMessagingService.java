package io.emaster.smashretrochat.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import io.emaster.smashretrochat.R;

/**
 * Created by elezermaster on 01/10/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notification_title = null == remoteMessage.getNotification().getTitle()? "":remoteMessage.getNotification().getTitle();
        String notification_body = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        String from_sender_id = remoteMessage.getData().get("from_sender_id").toString();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_if_chat_1891019)
                        .setContentTitle(notification_title)
                        .setContentText(notification_body);


        Intent result_intent = new Intent(click_action);
        result_intent.putExtra("visit_user_id", from_sender_id);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                result_intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        ///NotificationCompat.Builder mBuilder;
        ///...
        // Sets an ID for the notification
        int mNotificationId = (int) System.currentTimeMillis(); //001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
