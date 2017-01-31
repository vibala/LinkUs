package com.start_up.dev.apilinkus.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.start_up.dev.apilinkus.AfterNotificationActivity;
import com.start_up.dev.apilinkus.NotificationFriendRequestActivity;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;
import java.util.Map;

import static com.start_up.dev.apilinkus.Model.NotificationType.FRIEND_REQUEST;
import static com.start_up.dev.apilinkus.Model.NotificationType.MOMENT;

/**
 * Created by Huong on 10/12/2016.
 */

/**
 * Firebase has three message types:

 Notification messages :
 Notification message works on background or foreground. When app is in background, Notification messages are delivered to the system tray.
 If the app is in the foreground, messages are handled by onMessageReceived() or didReceiveRemoteNotification callbacks.
 These are essentially what is referred to as Display messages.

 Data messages:
 On Android platform, data message can work on background and foreground. The data message will be handled by onMessageReceived().
 A platform specific note here would be: On Android, the data payload can be retrieved in the Intent used to launch your activity.
 To elaborate, if you have "click_action":"launch_Activity_1", you can retrieve this intent through getIntent() from only Activity_1.

 Messages with both notification and data payloads:
 When in the background, apps receive the notification payload in the notification tray, and only handle the data payload when the user taps on the notification.
 When in the foreground, your app receives a message object with both payloads available.
 Secondly, the click_action parameter is often used in notification payload and not in data payload.
 If used inside data payload, this parameter would be treated as custom key-value pair and therefore you would need to implement custom logic for it to work as intended.

 Also, I recommend you to use onMessageReceived method (see Data message) to extract the data bundle.
 From your logic, I checked the bundle object and haven't found expected data content.

 Source
 http://stackoverflow.com/questions/39046270/google-fcm-getintent-dont-returning-expected-data-when-app-is-in-background-stat
 */
//#ADD
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static final String TAG = "Notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
            String title = "LinkUs";
            String type = data.get("type");

            if (type.equals(MOMENT)) {
                String momentId = data.get("momentId");
                String albumId = data.get("albumId");
                String body = data.get("title");

                Intent intent = new Intent(this, AfterNotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("albumId",albumId);

                //On créé une liste avec qu'un moment car la notificatoin ne contient qu'un seul moment
                ArrayList<String> listMomentId = new ArrayList<>();
                listMomentId.add(momentId);
                intent.putExtra("listMomentId",listMomentId);

                showNotification(title, body, intent);
            }
            if (type.equals(FRIEND_REQUEST)) {
                String fromFriendId = data.get("fromFriendId");
                String body = data.get("description");

                Intent intent = new Intent(this, NotificationFriendRequestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromFriendId",fromFriendId);

                showNotification(title, body,intent);
            }
        }
    }

    private void showNotification(String title,String body,Intent intent ) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.img_test)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

}