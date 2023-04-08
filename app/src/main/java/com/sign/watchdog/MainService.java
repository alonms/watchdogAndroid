package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;


public class MainService extends Service {
    private static final int MESSAGE_RESTART_PLAYER = 100;

    private Context mContext;
    private String toastMsg = "";
    private AlarmThread alarmThread;
    long intervalTime = 0;
    long nextTime = 0;
    long time1 = 0;
    long time2 = 0;
    long dur1 = 0;
    long dur2 = 0;
    boolean running = true;


    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        try {
            Log.d("Watchdog", "MainService started");
            mContext = MainService.this;
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);


            alarmThread = new AlarmThread();
            alarmThread.start();

        } catch (Exception e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    private void restartPlayer() {
        try {
            Log.d("Watchdog", "restartPlayer");
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent2.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            startActivity(intent2);
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }

    }


    public Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_RESTART_PLAYER:
                    restartPlayer();
                    break;
            }
            super.handleMessage(msg);
        }
    };




    public class AlarmThread extends Thread
    {
        private void updateLastTimeStamp() {
            try {
                UsageStatsManager usm = (UsageStatsManager)mContext.getSystemService("usagestats");
                long currentTime = System.currentTimeMillis();
                Map<String, UsageStats> appMap = usm.queryAndAggregateUsageStats(0, currentTime);
                // UsageStats usageStats = appMap.get("com.sec.android.app.sbrowser");   // S21
                UsageStats usageStats = appMap.get("com.android.chrome");
                if (usageStats!=null) {
                    //time1 = (currentTime - usageStats.getLastTimeStamp()) / 1000;
                    time1 = (currentTime - usageStats.getLastTimeUsed()) / 1000;
                }
            } catch (Exception e) {
                Log.e("Watchdog", e.getMessage());
            }
        }


        public void run()
        {
            Log.d("Watchdog", "Thread started");
            try {

                mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                sleep(10000);
            } catch (Exception e) {

            }



            while (true) {
                try {
                    Log.d("Watchdog", "time1="+String.valueOf(time1)+" time2="+String.valueOf(time2));
                    if (time1 < time2) {
                        mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                    }
                    time2 = time1;
                    sleep(5000);
                    updateLastTimeStamp();
                } catch (Exception e) {

                }
            }
        }

    }
}
