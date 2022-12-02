package com.sign.watchdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!Settings.canDrawOverlays(getApplicationContext())) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        }


        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        int rebootsPerDay = userDetails.getInt("rebootsPerDay", 1);
    }
}
