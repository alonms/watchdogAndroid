package com.sign.watchdog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;

public class WatchdogWebSocket extends WebSocketServer {
    Context mContext;
    Boolean normalExit = false;

    public WatchdogWebSocket(Context context) {
        super(new InetSocketAddress(5555));
        Log.d("WebSocketServer", "WatchdogWebSocket");
        mContext = context;
        setReuseAddr(true);
    }

    @Override
    public void onStart() {
        Log.d("WebSocketServer", "onStart");
        restartPlayer();
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d("WebSocketServer", "onOpen");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d("WebSocketServer", "onClose");
        if (normalExit==false) {
            restartPlayer();
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            Log.d("WebSocketServer", "onMessage=" + message);
            JSONObject json = new JSONObject(message);
            String command = json.getString("command");
            Log.d("WebSocketServer", "onMessage=" + command);
            if (command.equals("close")) {
                normalExit = true;
                Log.d("WebSocketServer", "System.exit");
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d("WebSocketServer", "onError " + ex.getMessage());
    }


    private void restartPlayer() {
        try {
            Log.d("Watchdog", "restartPlayer");
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //intent2.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
            mContext.startActivity(intent2);
        } catch (Exception e) {
            Log.e("Watchdog", e.getMessage());
        }

    }

}