package com.sign.watchdog;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class BootDeviceReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
            Toast.makeText(context, "BootDeviceReceiver v4", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            context.startActivity(intent2);

            /*
            Intent intent2 = new Intent(context, MainService.class);
            context.startService(intent2);

             */

        }
    }
}
