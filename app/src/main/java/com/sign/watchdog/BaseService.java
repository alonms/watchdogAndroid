package com.sign.watchdog;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.Map;


public class BaseService extends Service {
    private static final int MESSAGE_RESTART_PLAYER = 100;
    private static final int MESSAGE_TOAST = 101;

    protected CharSequence msg1;



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


    public BaseService() {
    }

    @Override
    public void onCreate() {
        alarmThread = new AlarmThread();
        alarmThread.start();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected void startThread(Context context)
    {
        WatchdogWebSocket wsServer = new WatchdogWebSocket(context);
        wsServer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }



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
