package com.sign.watchdog;

import android.app.usage.UsageStats;
import android.util.Log;

import java.util.Map;


public class MainServiceLatest extends BaseService {
    public MainServiceLatest() {
        super();
    }

    @Override
    public void onCreate()
    {
        try {
            Log.d("Watchdog", "MainService");
            mContext = MainServiceLatest.this;
            startThread();
        } catch (Exception e) {
        }
    }

    protected void startThread()
    {
        try {
            thread = new ThreadLatest();
            thread.start();
        } catch (Exception e) {
        }
    }

    public class ThreadLatest extends BaseService.ThreadBase {

        @Override
        protected long updateTime(Map<String, UsageStats> appMap, long currentTime, String key) {
            try {
                if (appMap.containsKey(key)) {
                    UsageStats usageStats = appMap.get(key);
                    if (usageStats != null) {
                        return (currentTime - usageStats.getLastTimeUsed()) / 1000;
                    }
                }
            } catch (Exception e) {

            }
            return 0;
        }
    }
}
