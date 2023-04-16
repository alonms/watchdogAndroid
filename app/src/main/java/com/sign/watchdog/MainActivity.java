package com.sign.watchdog;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.usage.ConfigurationStats;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity  {
    public static final int REQUEST_CODE = 1;


    public void showRunningApps() {
        try {
            String str = "";
            UsageStatsManager usm = (UsageStatsManager)this.getSystemService("usagestats");
            long currentTime = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, currentTime);
            for(int i=0; i<appList.size(); i++) {
                UsageStats usageStats = appList.get(i);
                long t1 = (currentTime - usageStats.getLastTimeStamp()) / 1000;
                long t2 = ( currentTime - usageStats.getLastTimeUsed()) / 1000;

                if (t1 > 0 && t1 < 60 ) {
                    str += usageStats.getPackageName() + " " + String.valueOf(t1)  + " " + String.valueOf(t2)  + "\n";

                }
            }
            Log.d("WD", str);
            //TextView msg = findViewById(R.id.textView2);
            //msg.setText(str);

        } catch (Exception e) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");

        WatchdogWebSocket wsServer = new WatchdogWebSocket();
        wsServer.start();



        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        int rebootsPerDay = userDetails.getInt("rebootsPerDay", 1);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        switch (rebootsPerDay) {
            case 0:
                radioGroup.check(R.id.radioButton0);
                break;
            case 1:
                radioGroup.check(R.id.radioButton1);
                break;
            case 2:
                radioGroup.check(R.id.radioButton2);
                break;
            case 3:
                radioGroup.check(R.id.radioButton3);
                break;
            case 4:
                radioGroup.check(R.id.radioButton4);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                SharedPreferences.Editor editor = userDetails.edit();
                editor.putInt("rebootsPerDay", index);
                editor.commit();
            }
        });

        requestDrawOverlays();
        requestUsageStats();
    }

    private void requestDrawOverlays() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            ActivityResultLauncher<Intent> ativityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        TextView msg = findViewById(R.id.textView2);

                        if (Settings.canDrawOverlays(getApplicationContext())) {
                            deploy();
                        } else {
                            msg.setText("signWatchdog is not configure properly, please restart the app and then select \"Appear on top\" for signWatchdog");
                            msg.setTextColor(Color.RED);
                        }
                    }
                });
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            ativityResultLauncher.launch(intent);
        }
    }

    private void requestUsageStats() {
        final UsageStatsManager mUsageStatsManager = (UsageStatsManager)this.getSystemService(Context.USAGE_STATS_SERVICE);
        final long now = System.currentTimeMillis();
        final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now);
        boolean granted = (stats != null && !stats.isEmpty());
        if (granted==false) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private void deploy() {
        String urlString = "https://galaxy.signage.me/deploy/deploy.html";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
        }
    }


    private void updateLastTimeStamp() {
        try {
            UsageStatsManager usm = (UsageStatsManager)getSystemService("usagestats");
            long currentTime = System.currentTimeMillis();
            Map<String, UsageStats> appMap = usm.queryAndAggregateUsageStats(0, currentTime);
            UsageStats usageStats = appMap.get("com.android.chrome");
            if (usageStats!=null) {
                long time1 = (currentTime - usageStats.getLastTimeUsed()) / 1000;
                //long time2 = (currentTime - usageStats.getFirstTimeStamp()) / 1000;

                long time2 = usageStats.getTotalTimeInForeground() / 1000;
                Log.d("WD", String.valueOf(time2));
            }
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }
    }

    public class MyThread extends Thread
    {
        public void run()
        {
            while(true) {
                updateLastTimeStamp();
             try {
                 sleep(1000);
             } catch (Exception e) {

             }

            }
        }
    }

}
