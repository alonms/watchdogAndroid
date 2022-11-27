package com.sign.watchdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetSocketAddress;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    Button mButton;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);
        txt = findViewById(R.id.msg);

        /*
        // ws://10.0.2.16:38301
        String ipAddress = "192.168.0.117";
        InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, 38301);
        WebsocketServer wsServer = new WebsocketServer(inetSockAddress);
        wsServer.setReuseAddr(true);
        wsServer.start();
        */
    }


    @Override
    public void onClick(View var1) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://dev.signage.me/installplayer/"));
        startActivity(intent);
    }


    public void startStartAWebApp(String startUrl, int sourceFlag, String packageName) {
        Intent intent = new Intent("com.google.android.apps.chrome.webapps.WebappManager.ACTION_START_WEBAPP");
        intent.putExtra("org.chromium.chrome.browser.webapp_url", startUrl); // String
        intent.putExtra("org.chromium.chrome.browser.webapp_source", sourceFlag); // int
        intent.putExtra("org.chromium.chrome.browser.webapk_package_name", packageName); // String
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
