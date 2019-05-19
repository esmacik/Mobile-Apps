package com.techexchange.mobileapps.lab12;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

final class NotificationHandler extends Handler {

    private static final String TAG = "NotificationHandler";

    private Context context;

    NotificationHandler(Context context){
        this.context = context;
    }

    private static final String NOTIFICATION_COUNTER = "NOTIFICATION_COUNTER";

    @Override
    public void handleMessage(Message msg){
        SharedPreferences sharedPrefs = context.getSharedPreferences(NOTIFICATION_COUNTER, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        int notificationNumber = sharedPrefs.getInt(NOTIFICATION_COUNTER, 0);

        Notification notification = new NotificationCompat.Builder(context,
                NotifierService.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Ping Notification")
                .setContentText("This is your 30-second reminder number: " + notificationNumber)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(0, notification);
        editor.putInt(NOTIFICATION_COUNTER, notificationNumber + 1);
        editor.commit();

        Message message = new Message();
        message.copyFrom(msg);

        Handler handler = new NotificationHandler(context);
        handler.sendMessageDelayed(message, 30000);
    }
}