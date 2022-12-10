package com.sign.watchdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!Settings.canDrawOverlays(getApplicationContext())) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        }


/*
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)){
            Toast.makeText(this,"permission RECEIVE_BOOT_COMPLETED enabled",Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, 1);
        }
*/





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

        //startAlarm(this);
    }

    private void startAlarm(Context context)
    {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            PendingIntent pendingIntent = (PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE));
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
                while(startTime<currentTime) {
                    startTime += intervalTime;
                }
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalTime, pendingIntent);
            }
            pendingIntent.send();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
