package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
        Toast.makeText(context, "Watchdog 1.0.0", Toast.LENGTH_LONG).show();
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            startServiceByAlarm(context);
        }
    }

    private void startServiceByAlarm(Context context)
    {
        try {
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            PendingIntent pendingIntent = (PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startTime = calendar.getTimeInMillis();
            if (startTime < System.currentTimeMillis()) {
                startTime += AlarmManager.INTERVAL_DAY;
            }
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);



            //???long intervalTime = AlarmManager.INTERVAL_DAY;
            long intervalTime = 30000;


            Toast.makeText(context, "Restart Player every day at 12AM", Toast.LENGTH_LONG).show();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);

            pendingIntent.send();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
