package com.sign.watchdog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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
        if (!hasUsageStats()) {
            ActivityResultLauncher<Intent> ativityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    TextView msg = findViewById(R.id.textView2);
                    if (hasUsageStats()) {

                    } else {
                        msg.setText("signWatchdog is not configure properly, please restart the app and then select \"Usage access\" for signWatchdog");
                        msg.setTextColor(Color.RED);
                    }
                });
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            ativityResultLauncher.launch(intent);
        }
    }
}
