package com.sign.watchdog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import androidx.core.app.NotificationCompat;


public class MainServiceAdz extends BaseService {
    public MainServiceAdz() {
        super();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        try {
            Log.d("Watchdog", "ForegroundService");
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"title", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);

            startThread(MainServiceAdz.this);
        } catch (Exception e) {
        }
    }

}


