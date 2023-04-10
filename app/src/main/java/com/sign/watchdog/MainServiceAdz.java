package com.sign.watchdog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Map;


public class MainServiceAdz extends BaseService {
    public MainServiceAdz() {
        super();
    }

    @Override
    public void onCreate()
    {
        try {
            Log.d("Watchdog", "ForegroundService");
            mContext = MainServiceAdz.this;
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"title", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);

            startThread();
        } catch (Exception e) {
        }
    }

    protected void startThread()
    {
        try {
            thread = new ThreadAdz();
            thread.start();
        } catch (Exception e) {
        }
    }

    public class ThreadAdz extends BaseService.ThreadBase {

        @Override
        protected long updateTime(Map<String, UsageStats> appMap, long currentTime, String key) {
            try {
                if (appMap.containsKey(key)) {
                    UsageStats usageStats = appMap.get(key);
                    if (usageStats != null) {
                        return (currentTime - usageStats.getLastTimeUsed()) / 1000;
                    }
                }
            } catch (Exception e) {

            }
            return 0;
        }
    }
}


