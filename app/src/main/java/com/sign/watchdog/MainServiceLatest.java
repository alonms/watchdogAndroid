package com.sign.watchdog;


import android.util.Log;


public class MainServiceLatest extends BaseService {
    public MainServiceLatest() {
        super();
    }

    @Override
    public void onCreate()
    {
        try {
            Log.d("Watchdog", "MainService");
            startThread(MainServiceLatest.this);
        } catch (Exception e) {
        }
    }

}
