package com.sign.watchdog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;


public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        //Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        createNotification(this);
        try {
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            startActivity(intent2);
        } catch (Exception e) {
            Toast.makeText(this, "Fail: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    private void createNotification(Context context) {
        NotificationChannel channel=new NotificationChannel("channel1","restart", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this,"channel1");
        notification.setContentTitle("signWatchdog");
        notification.setContentText("restarting signPlayer");
        notification.setSmallIcon(R.drawable.ic_launcher_foreground);
        manager.notify(121,notification.build());
    }
}