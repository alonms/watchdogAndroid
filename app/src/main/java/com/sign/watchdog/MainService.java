package com.sign.watchdog;

import android.app.AlarmManager;
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
import android.widget.Toast;

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
    private static final int MESSAGE_TOAST = 101;
    private Context mContext;
    private String toastMsg = "";
    private AlarmThread alarmThread;
    long intervalTime = 0;
    long nextTime = 0;
    long time1 = 0;
    long time2 = 0;
    long dur1 = 0;
    long dur2 = 0;


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
            Toast.makeText(this, "MainService.onCreate()", Toast.LENGTH_SHORT).show();
            restartPlayer();
            /*
            mContext = MainService.this;
            alarmThread = new AlarmThread();
            alarmThread.start();

             */
        } catch (Exception e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "MainService.onStartCommand()", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }


    private void restartPlayer() {
        Toast.makeText(this, "restartPlayer()", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_VIEW);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent2.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
        startActivity(intent2);
    }

    private void showToast() {
        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
    }

    public Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_RESTART_PLAYER:
                    restartPlayer();
                    break;
                case MESSAGE_TOAST:
                    showToast();
                    break;
            }
            super.handleMessage(msg);
        }
    };




    public class AlarmThread extends Thread
    {
        public boolean isAppRunning() {
            try {
                UsageStatsManager usm = (UsageStatsManager)mContext.getSystemService("usagestats");
                long currentTime = System.currentTimeMillis();
                Map<String, UsageStats> appMap = usm.queryAndAggregateUsageStats(0, currentTime);
                // UsageStats usageStats = appMap.get("com.sec.android.app.sbrowser");   // S21
                UsageStats usageStats = appMap.get("com.android.chrome");
                if (usageStats==null) {
                    return false;
                }
                dur2 = dur1;
                time2 = time1;
                time1 = (currentTime - usageStats.getLastTimeStamp()) / 1000;
                dur1 = usageStats.getTotalTimeVisible() / 1000;
                if (time1 < time2 && dur1 > dur2) {
                    return false;
                }
            } catch (Exception e) {

            }
            return true;
        }


        public void run()
        {
            try {
                mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                sleep(20000);
            } catch (Exception e) {

            }

            while (true) {
                try {
                    sleep(5000);
                    toastMsg = "ping";
                    mMessageHandler.sendEmptyMessage(MESSAGE_TOAST);
                    /*
                    if (!isAppRunning()) {
                        mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                    }

                     */
                } catch (Exception e) {

                }
            }
        }

    }
}