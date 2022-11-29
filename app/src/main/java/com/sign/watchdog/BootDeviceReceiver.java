package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.util.Calendar;

public class BootDeviceReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "v10", Toast.LENGTH_LONG).show();
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
            Toast.makeText(context, "ACTION_BOOT_COMPLETED", Toast.LENGTH_LONG).show();
            try {
                startServiceByAlarm(context);
            } catch (Exception e) {

            }
        }
    }

    private void startServiceByAlarm(Context context)
    {
        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Create intent to invoke the background service.
        Intent intent = new Intent(context, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        long startTime = System.currentTimeMillis();
        long intervalTime = 60*1000;

        Toast.makeText(context, "Create Alarm", Toast.LENGTH_LONG).show();

        // Create repeat alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
    }

}
