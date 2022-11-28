package com.sign.watchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Toast.makeText(context, "AlarmReceiver", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent();
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            context.startActivity(intent2);
        } catch (Exception e) {
            int a=3;
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}