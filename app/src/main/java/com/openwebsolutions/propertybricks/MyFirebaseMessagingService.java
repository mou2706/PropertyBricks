package com.openwebsolutions.propertybricks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    int messageCount;

    @Override
    public String toString() {
        return "MyFirebaseMessagingService{}";
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0){

            //handle the data message here

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
//        String notification=remoteMessage.getNotification().getChannelId();
//        String offer_notification_key=remoteMessage.getData().get("offersnotifications");
        String counter_key=remoteMessage.getData().get("counter");
        String title_key=remoteMessage.getData().get("title");

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString("offersnotifications",offer_notification_key);
        editor.putString("counter",counter_key);
        editor.commit();





        MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title,body);
//        MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title_key,body);


        Intent intent= new Intent(this,SpashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("FCM Notification");
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setNumber(messageCount);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
//
//        /*
//         * Displaying a notification locally
//         */

        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");

        //then here we can use the title and body to build a notification
    }
}
