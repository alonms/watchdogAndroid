package com.sign.watchdog;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String message = "RunAfterBootService onStartCommand() method.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_VIEW);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
        startActivity(intent2);

        return super.onStartCommand(intent, flags, startId);
    }

}