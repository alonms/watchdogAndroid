package com.sign.watchdog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;


public class MainService extends BaseService {
    public MainService() {
        super();
    }

    @Override
    public void onCreate()
    {
        try {
            Log.d("Watchdog", "MainService");
            mContext = MainService.this;
            startThread();
        } catch (Exception e) {
        }
    }
}
