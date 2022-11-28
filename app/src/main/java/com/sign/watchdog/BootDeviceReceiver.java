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
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
            Toast.makeText(context, "BootDeviceReceiver v5", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            context.startActivity(intent2);

            try {
                alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent intent3 = new Intent(context, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_IMMUTABLE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 17);
                calendar.set(Calendar.MINUTE, 15);
                calendar.set(Calendar.SECOND, 0);
                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 30000, alarmIntent);
            } catch (Exception e) {
                int a = 3;
            }

        }
    }
}
