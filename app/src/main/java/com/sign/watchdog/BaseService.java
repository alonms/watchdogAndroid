package com.sign.watchdog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Map;


public class BaseService extends Service {
    private static final int MESSAGE_RESTART_PLAYER = 100;
    private static final int MESSAGE_TOAST = 101;

    protected Context mContext;
    protected ThreadBase thread;
    protected long intervalTime = 0;
    protected long nextTime = 0;
    protected long time1 = 0;
    protected long time2 = 0;
    protected long dur1 = 0;
    protected long dur2 = 0;
    protected boolean running = true;
    protected CharSequence msg1;


    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void startThread()
    {
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

                case MESSAGE_TOAST:
                    Toast.makeText(mContext, msg1, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };




    public class ThreadBase extends Thread
    {
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

        private void updateLastTimeStamp() {
            try {
                UsageStatsManager usm = (UsageStatsManager)mContext.getSystemService(Context.USAGE_STATS_SERVICE);
                long currentTime = System.currentTimeMillis();

                Map<String, UsageStats> appMap = usm.queryAndAggregateUsageStats(0, currentTime);

                time1 = updateTime(appMap,currentTime, "com.android.chrome"); // ADZ
                //long t2 = updateTime(appMap,currentTime, "com.sec.android.app.sbrowser"); // S21
                //time1 = Math.max(t1, t2);
            } catch (Exception e) {
                Log.e("Watchdog", e.getMessage());
            }
        }


        public void run()
        {
            Log.d("Watchdog", "Thread started");
            try {
                sleep(5000);
                mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                sleep(5000);
            } catch (Exception e) {

            }

            while (true) {
                try {
                    Log.d("Watchdog", "time1="+String.valueOf(time1)+" time2="+String.valueOf(time2));
                    msg1 = "time1="+String.valueOf(time1)+" time2="+String.valueOf(time2);
                    mMessageHandler.sendEmptyMessage(MESSAGE_TOAST);

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
