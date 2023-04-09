package com.sign.watchdog;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

public class BootDeviceReceiver extends BroadcastReceiver {
    private Context mContext;
    Intent intent;
    PendingIntent restartPlayerIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("Watchdog", "onReceive");
            mContext = context;
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            Toast.makeText(context, "signWatchdog " + info.versionName, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "signWatchdog NA", Toast.LENGTH_LONG).show();
        }
        createRestartIntent(context);
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Toast.makeText(context, "ACTION_BOOT_COMPLETED", Toast.LENGTH_LONG).show();
            startAlarm(context);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // ADZ
                    Log.d("Watchdog", "ADZ");
                    Intent serviceIntent = new Intent(context, MainService.class);
                    context.startForegroundService(serviceIntent);

                } else {
                    // S21
                    Log.d("Watchdog", "S21");
                    Intent serviceIntent = new Intent(context, MainService.class);
                    context.startService(serviceIntent);
                }
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
            }
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }
    }
}
