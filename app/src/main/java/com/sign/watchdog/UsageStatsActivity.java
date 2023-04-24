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
import java.util.Timer;
import java.util.TimerTask;

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
        if (!hasUsageStats()) {
            ActivityResultLauncher<Intent> ativityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    TextView msg = findViewById(R.id.msg0);

                    if (hasUsageStats()) {
                        try {
                            Intent drawOverlaysIntent = new Intent(this, DrawOverlaysActivity.class);
                            PendingIntent drawOverlaysPendingIntent = (PendingIntent.getActivity(this, 0, drawOverlaysIntent, PendingIntent.FLAG_IMMUTABLE));

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        drawOverlaysPendingIntent.send();
                                    } catch (Exception e) {

                                    }
                                }
                            }, 1000);
                        } catch (Exception e) {

                        }
                    } else {
                        msg.setText("signWatchdog is not configure properly, please restart the app and then select \"Usage access\" for signWatchdog");
                        msg.setTextColor(Color.RED);
                    }
                });
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            ativityResultLauncher.launch(intent);
        } else {
            try {
                Intent drawOverlaysIntent = new Intent(this, DrawOverlaysActivity.class);
                PendingIntent drawOverlaysPendingIntent = (PendingIntent.getActivity(this, 0, drawOverlaysIntent, PendingIntent.FLAG_IMMUTABLE));
                drawOverlaysPendingIntent.send();
            } catch (Exception e) {

            }
        }
    }
}
