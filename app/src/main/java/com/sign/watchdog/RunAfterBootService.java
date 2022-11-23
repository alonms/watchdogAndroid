package com.sign.watchdog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.net.InetSocketAddress;

public class RunAfterBootService extends Service {

    private static final String TAG_BOOT_EXECUTE_SERVICE = "BOOT_BROADCAST_SERVICE";

    public RunAfterBootService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_BOOT_EXECUTE_SERVICE, "RunAfterBootService onCreate() method.");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String message = "RunAfterBootService onStartCommand() method.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_EXECUTE_SERVICE, "RunAfterBootService onStartCommand() method.");




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}