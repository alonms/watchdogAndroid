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


        //startMainService(this);
    }




    private void startMainService(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Create intent to invoke the background service.
            Intent intent = new Intent(context, MainService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            long startTime = System.currentTimeMillis();
            long intervalTime = 10 * 1000;
            Toast.makeText(context, "Set Alarm", Toast.LENGTH_LONG).show();

            // Create repeat alarm.
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Fail: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


}
