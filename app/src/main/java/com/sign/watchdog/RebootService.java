package com.sign.watchdog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;


public class RebootService extends Service {
    public RebootService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        SharedPreferences userDetails = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        boolean hardReboot = userDetails.getBoolean("hardReboot", true);
        if (hardReboot) {
            rebootDevice();
        } else {
            restartPlayer();
        }
    }

    private void rebootDevice() {
        try {
            Log.d("Watchdog", "Reboot device");
            Runtime.getRuntime().exec("reboot");
        } catch (Exception e) {
            restartPlayer();
        }
    }

    private void restartPlayer() {
        try {
            Log.d("Watchdog", "restartPlayer");
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent2.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            //intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            startActivity(intent2);
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }

    }
}