package com.sign.watchdog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


public class BaseService extends Service {
    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected void startThread(Context context)
    {
        WatchdogWebSocket wsServer = new WatchdogWebSocket(context);
        wsServer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }
}
