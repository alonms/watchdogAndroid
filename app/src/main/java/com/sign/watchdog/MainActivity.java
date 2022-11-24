package com.sign.watchdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetSocketAddress;

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
        txt.setText("aaa");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent .setPackage("com.sign.watchdog");
        intent.putExtra(Intent.EXTRA_TEXT, Uri.parse("alon"));
        intent.setType("text/plain");
        startActivity(intent);
    }
}
