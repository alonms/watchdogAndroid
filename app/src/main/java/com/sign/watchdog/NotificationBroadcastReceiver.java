package com.sign.watchdog;


import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context , Intent intent) {
        Toast.makeText(context, "WD: Receiver!!!", Toast.LENGTH_LONG).show();
    }
}



