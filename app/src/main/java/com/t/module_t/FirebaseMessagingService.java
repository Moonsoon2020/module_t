package com.t.module_t;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String TAG = "TestFbseMsgngSvc";

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, remoteMessage.getData().toString());
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String val1 = remoteMessage.getData().get("val1");
            String val2 = remoteMessage.getData().get("val2");
            String val3 = remoteMessage.getData().get("val3");
            int color = (1 << 16) | (1 << 8) | (0);
            showNotification(val1, val2, color);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send the new token to your app server, do that here.
    }

    @Override
    public void onDeletedMessages() {
        // In some situations, FCM may not deliver a message. This occurs when there are too many messages (>100) pending for your app on a particular device
        // at the time it connects or if the device hasn't connected to FCM in more than one month. In these cases, you may receive a callback
        // to FirebaseMessagingService.onDeletedMessages() When the app instance receives this callback, it should perform a full sync with your app server.
        // If you haven't sent a message to the app on that device within the last 4 weeks, FCM won't call onDeletedMessages().
    }

    void showNotification(String title, String text, int color) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.baseline_check_24)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(color, 100, 200);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1001;
        try {
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
