package com.sign.watchdog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class BaseService extends Service {
    private static final int MESSAGE_RESTART_PLAYER = 100;
    private static final int MESSAGE_TOAST = 101;

    protected CharSequence msg1;


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
