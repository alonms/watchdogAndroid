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

    protected Context mContext;
    protected CharSequence msg1;


    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected void startThread()
    {
        WatchdogWebSocket wsServer = new WatchdogWebSocket();
        wsServer.start();
        try {
            Thread.sleep(1000);
            restartPlayer();
        } catch (Exception e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }


    private void restartPlayer() {
        try {
            Log.d("Watchdog", "restartPlayer");
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent2.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            startActivity(intent2);
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }

    }


    public Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_RESTART_PLAYER:
                    restartPlayer();
                    break;

                case MESSAGE_TOAST:
                    Toast.makeText(mContext, msg1, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
