package com.sign.watchdog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MainService extends Service {
    private Context mContext;
    private Helper mHelper;

    public MainService() {
    }


    @Override
    public void onCreate()
    {
        mContext = MainService.this;
        Toast.makeText(mContext, "Start SignageWatchdog 4.33.12", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}