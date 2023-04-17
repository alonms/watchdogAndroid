package com.sign.watchdog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;


public class WatchdogWebSocket extends WebSocketServer {
    public static int MESSAGE_RESTART_PLAYER = 0x0100;
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
        //??? restartPlayer();
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d("WebSocketServer", "onOpen");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d("WebSocketServer", "onClose");
        if (normalExit==false) {
            //??? restartPlayer();
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            Log.d("WebSocketServer", "onMessage=" + message);
            JSONObject receivedMessage = new JSONObject(message);
            JSONObject result;
            String command = receivedMessage.getString("command");
            Log.d("WebSocketServer", "onMessage=" + command);
            if (command.equals("start")) {
                JSONObject data = new JSONObject();
                data.put("extension", "1.6.0");
                data.put("native", "1.6.0");
                result = getResult(receivedMessage, data.toString());
                conn.send(result.toString());
            } else if (command.equals("ping")) {
                result = getResult(receivedMessage, "pong");
                conn.send(result.toString());
            } else if (command.equals("close")) {
                Log.d("WebSocketServer", "normalExit");
                normalExit = true;
                result = getResult(receivedMessage, "closed");
                conn.send(result.toString());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d("WebSocketServer", "onError " + ex.getMessage());
    }


    private static JSONObject getResult(JSONObject message, String data)
    {
        try {
            JSONObject result = new JSONObject();
            result.put("source", "signPlayerWatchdog");
            result.put("messageId", message.getString("messageId"));
            result.put("result", data);
            return result;
        }
        catch (Exception e) {
            return null;
        }
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


    public Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_RESTART_PLAYER:
                    restartPlayer();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public class ThreadBase extends Thread
    {
        public void run()
        {
            Log.d("Watchdog", "Thread started");
            while (true) {
                try {
                    mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                    sleep(5000);
                } catch (Exception e) {

                }
            }
        }
    }
}