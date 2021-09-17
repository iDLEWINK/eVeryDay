package com.mobdeve.s14.group24.everyday;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Set the intent to be opened upon clicking the notification
        Intent notificationActivityIntent = new Intent (context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Initialize NotificationManager object
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Device SDK is later than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Initialize NotificationChannel object
            NotificationChannel channel = new NotificationChannel(Keys.NOTIFICATION_CHANNEL_ID.name(), Keys.NOTIFICATION_CHANNEL_NAME.name(), NotificationManager.IMPORTANCE_HIGH);

            //Assign NotificationChannel to the Notification channel object
            notificationManager.createNotificationChannel(channel);
        }

        //Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Keys.NOTIFICATION_CHANNEL_ID.name())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Capture Your Precious Memories")
                .setContentText("Please \uD83D\uDC49\uD83D\uDC48");

        //Show the notification
        notificationManager.notify(0, builder.build());
    }
}
