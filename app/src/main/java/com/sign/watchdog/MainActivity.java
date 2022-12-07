package com.sign.watchdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import java.util.Date;
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


        //startMainService(this);
        //createNotification(this);
        //test();
    }

    private void test() {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        long intervalTime = AlarmManager.INTERVAL_DAY / 3;
        long currentTime = System.currentTimeMillis() + tz.getRawOffset();
        long startTime = (((long) (currentTime / intervalTime)) + 1L) * intervalTime;
        long minutes = (startTime - currentTime) / 60000;

    }


    private void createNotification(Context context) {
        //Creating a notification channel
        NotificationChannel channel=new NotificationChannel("channel1",
                "hello",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        //Creating the notification object
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this,"channel1");
        //notification.setAutoCancel(true);
        notification.setContentTitle("Hi this is a notification");
        notification.setContentText("Hello you");
        notification.setSmallIcon(R.drawable.ic_launcher_foreground);

        //make the notification manager to issue a notification on the notification's channel
        manager.notify(121,notification.build());
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
