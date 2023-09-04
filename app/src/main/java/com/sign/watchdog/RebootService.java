package com.sign.watchdog;

import android.app.Service;
import android.content.Intent;
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
        try {
            Log.d("Watchdog", "Reboot device");
            Runtime.getRuntime().exec("reboot");
        } catch (Exception e) {
            restartPlayer();
        }
    }

    private void restartPlayer() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
        startActivity(intent);
    }
}