package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MainService extends Service {
    private static final int MESSAGE_RESTART_PLAYER = 100;
    private Context mContext;
    //private AlarmThread alarmThread;
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
        Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();

        createNotification(this);

        try {
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            startActivity(intent2);
        } catch (Exception e) {
            Toast.makeText(this, "Fail: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }


    private void createNotification(Context context) {
        //Creating a notification channel
        NotificationChannel channel=new NotificationChannel("channel1",
                "hello",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        //Creating the notification object
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this,"channel1");
        //notification.setAutoCancel(true);
        notification.setContentTitle("Hi this is a notification");
        notification.setContentText("Hello you");
        notification.setSmallIcon(R.drawable.ic_launcher_foreground);

        //make the notification manager to issue a notification on the notification's channel
        manager.notify(121,notification.build());
    }

/*
    public Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_RESTART_PLAYER:
                    Toast.makeText(mContext, "MESSAGE_RESTART_PLAYER", Toast.LENGTH_SHORT).show();
                    try {
                        Intent intent2 = new Intent();
                        intent2.setAction(Intent.ACTION_VIEW);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
                        startActivity(intent2);
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Fail: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

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

*/
}