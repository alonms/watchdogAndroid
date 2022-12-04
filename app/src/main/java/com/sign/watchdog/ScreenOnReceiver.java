package com.sign.watchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenOnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Screen ON", Toast.LENGTH_LONG).show();
        //Intent intent2 = new Intent(context, MainService.class);
        //context.startService(intent2);
        startMainService(context.getApplicationContext());
    }

    private void startMainService(Context context) {
        Intent intent = new Intent(context, MainService.class);
        context.startService(intent);
    }
}