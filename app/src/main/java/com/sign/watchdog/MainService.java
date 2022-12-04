package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;


public class MainService extends Service {
    private static final int MESSAGE_RESTART_PLAYER = 100;
    private Context mContext;
    private AlarmThread alarmThread;
    long intervalTime = 0;
    long nextTime = 0;

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

            mContext = MainService.this;

            SharedPreferences userDetails = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            long rebootsPerDay = userDetails.getInt("rebootsPerDay", 1);
            if (rebootsPerDay > 0) {
                if (rebootsPerDay == 4)
                    rebootsPerDay = 24 * 60;
                intervalTime = AlarmManager.INTERVAL_DAY / rebootsPerDay;
                alarmThread = new AlarmThread();
                alarmThread.start();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "MainService.onStartCommand()", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    /*
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sendBroadcast(new Intent("YouWillNeverKillMe"));
        Toast.makeText(this, "YouWillNeverKillMe TOAST!!", Toast.LENGTH_LONG).show();
    }

     */


    public Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_RESTART_PLAYER:
                    Toast.makeText(mContext, "MESSAGE_RESTART_PLAYER", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_VIEW);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
                    startActivity(intent2);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public class AlarmThread extends Thread
    {
        public void run()
        {
            while (true) {
                try {

                    long currentTime = System.currentTimeMillis();
                    if (currentTime > nextTime) {
                        nextTime = (((long)(currentTime / intervalTime)) + 1L) * intervalTime;
                        mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                    }
                    sleep(5000);
                } catch (Exception e) {

                }
            }
        }
    }
}