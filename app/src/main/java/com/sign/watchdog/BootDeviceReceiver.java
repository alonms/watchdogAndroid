package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class BootDeviceReceiver extends BroadcastReceiver {
    private Context mContext;
    Intent intent;
    PendingIntent restartPlayerIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("Alon", "onReceive");
            mContext = context;
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            Toast.makeText(context, "signWatchdog " + info.versionName, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "signWatchdog NA", Toast.LENGTH_LONG).show();
        }
        Log.d("Watchdog", "test1");

        createRestartIntent(context);



        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Toast.makeText(context, "ACTION_BOOT_COMPLETED", Toast.LENGTH_LONG).show();
            startAlarm(context);
            Log.d("Watchdog", "test2");

            try {
                Intent serviceIntent = new Intent(context, MainService.class);
                context.startService(serviceIntent);  // S21
                //context.startForegroundService(serviceIntent);
            } catch (Exception e) {
                Log.d("Watchdog", e.getMessage());
            }


        }
    }

    private void createRestartIntent(Context context) {
        intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
        restartPlayerIntent = (PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE));
    }


    private void startAlarm(Context context)
    {
        try {
            SharedPreferences userDetails = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            long rebootsPerDay = userDetails.getInt("rebootsPerDay", 1);
            if (rebootsPerDay>0) {
                if (rebootsPerDay==4)
                    rebootsPerDay = 24;
                long intervalTime = AlarmManager.INTERVAL_DAY / rebootsPerDay;

                long currentTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTime);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long startTime = calendar.getTimeInMillis() - AlarmManager.INTERVAL_DAY;
                while(startTime < currentTime) {
                    startTime += intervalTime;
                }
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, restartPlayerIntent);

                // TEST
                //Intent serviceIntent = new Intent(context, MainService.class);
                //PendingIntent servicePendingIntent = (PendingIntent.getActivity(context, 0, serviceIntent, PendingIntent.FLAG_IMMUTABLE));
                //startTime = currentTime + 2 * 60 * 1000;
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, servicePendingIntent);
            }
            // restartPlayerIntent.send();
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }
    }
}
