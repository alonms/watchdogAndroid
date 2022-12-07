package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class BootDeviceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            Toast.makeText(context, "signWatchdog " + info.versionName, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "signWatchdog NA", Toast.LENGTH_LONG).show();
        }


        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Toast.makeText(context, "ACTION_BOOT_COMPLETED", Toast.LENGTH_LONG).show();
            //startAlarm(context);
            startMainService(context);
        }


    }

    private void startAlarm(Context context)
    {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //??? intent.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            intent.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            PendingIntent pendingIntent = (PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE));
            SharedPreferences userDetails = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE);
            long rebootsPerDay = userDetails.getInt("rebootsPerDay", 1);
            if (rebootsPerDay>0) {
                if (rebootsPerDay==4)
                    rebootsPerDay = 24;
                long intervalTime = AlarmManager.INTERVAL_DAY / rebootsPerDay;
                long currentTime = System.currentTimeMillis();
                long startTime = currentTime / intervalTime;
                startTime += 1L;
                startTime *= intervalTime;
                long diff = (startTime - currentTime) / 1000;
                long interval = intervalTime / 1000;
                Toast.makeText(context, "Start in: " + diff + " Interval: " + interval, Toast.LENGTH_LONG).show();
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
            }
            pendingIntent.send();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void startMainService(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Create intent to invoke the background service.
            Intent intent = new Intent(context, MainService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            long startTime = System.currentTimeMillis();
            long intervalTime = 10 * 1000;
            Toast.makeText(context, "Set Alarm", Toast.LENGTH_LONG).show();

            // Create repeat alarm.
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Fail: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
