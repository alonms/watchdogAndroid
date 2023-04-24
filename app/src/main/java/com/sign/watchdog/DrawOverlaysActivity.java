package com.sign.watchdog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DrawOverlaysActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_overlays);
        Log.d("DrawOverlaysActivity", "onCreate");
        requestDrawOverlays();
    }

    private void requestDrawOverlays() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            ActivityResultLauncher<Intent> ativityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        TextView msg = findViewById(R.id.textView2);

                        if (Settings.canDrawOverlays(getApplicationContext())) {
                            try {
                                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                                PendingIntent settingsIntentPendingIntent = (PendingIntent.getActivity(this, 0, settingsIntent, PendingIntent.FLAG_IMMUTABLE));
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                long currentTime = System.currentTimeMillis();
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime, AlarmManager.INTERVAL_DAY, settingsIntentPendingIntent);
                            } catch (Exception e) {

                            }
                        } else {
                            msg.setText("signWatchdog is not configure properly, please restart the app and then select \"Appear on top\" for signWatchdog");
                            msg.setTextColor(Color.RED);
                        }
                    });
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            ativityResultLauncher.launch(intent);

        } else {
            try {
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                PendingIntent settingsIntentPendingIntent = (PendingIntent.getActivity(this, 0, settingsIntent, PendingIntent.FLAG_IMMUTABLE));
                settingsIntentPendingIntent.send();
            } catch (Exception e) {

            }
        }
    }
}
