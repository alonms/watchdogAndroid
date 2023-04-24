package com.sign.watchdog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import java.util.List;

public class UsageStatsActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_stats);
        Log.d("UsageStatsActivity", "onCreate");
        requestUsageStats();
    }

    private boolean hasUsageStats() {
        final long now = System.currentTimeMillis();
        final UsageStatsManager mUsageStatsManager = (UsageStatsManager)this.getSystemService(Context.USAGE_STATS_SERVICE);
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);
        return (stats != null && !stats.isEmpty());
    }


    private void requestUsageStats() {
        Intent drawOverlaysIntent = new Intent(this, DrawOverlaysActivity.class);
        PendingIntent drawOverlaysPendingIntent = (PendingIntent.getActivity(this, 0, drawOverlaysIntent, PendingIntent.FLAG_IMMUTABLE));
        if (!hasUsageStats()) {
            ActivityResultLauncher<Intent> ativityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (hasUsageStats()) {
                        try {
                            long currentTime = System.currentTimeMillis() + 5000;
                            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime, AlarmManager.INTERVAL_DAY, drawOverlaysPendingIntent);
                        } catch (Exception e) {

                        }
                    }
                });
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            ativityResultLauncher.launch(intent);
        } else {
            try {
                drawOverlaysPendingIntent.send();
            } catch (Exception e) {

            }
        }
    }
}
