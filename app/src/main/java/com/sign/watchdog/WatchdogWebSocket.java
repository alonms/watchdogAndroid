package com.sign.watchdog;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.Map;


public class WatchdogWebSocket extends WebSocketServer {
    private static final int MESSAGE_RESTART_PLAYER = 100;
    private static final int MESSAGE_TOAST = 101;

    Context mContext;
    WatchdogThread watchdogThread = null;
    Boolean watchdogEnabled = false;
    boolean checkLastTimeStamp = true;
    int count = 0;
    String toast = "";
    long time1 = 0;
    long time2 = 0;

    public WatchdogWebSocket(Context context) {
        super(new InetSocketAddress(8090));
        Log.d("WebSocketServer", "WatchdogWebSocket");
        mContext = context;
        setReuseAddr(true);

        toast = "WebSocket onStart";
        mMessageHandler.sendEmptyMessage(MESSAGE_TOAST);

        watchdogThread = new WatchdogThread();
        watchdogThread.start();
        restartPlayer();
    }

    @Override
    public void onStart() {
        Log.d("WebSocketServer", "onStart");
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d("WebSocketServer", "onOpen");
        toast = "WebSocket onOpen";
        mMessageHandler.sendEmptyMessage(MESSAGE_TOAST);
        count = 0;
        watchdogEnabled = true;
        checkLastTimeStamp = false;
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d("WebSocketServer", "onClose");
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
                count = 0;
                result = getResult(receivedMessage, "pong");
                conn.send(result.toString());
            } else if (command.equals("close")) {
                Log.d("WebSocketServer", "normalExit");
                watchdogEnabled = false;
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
            intent2.setData(Uri.parse("https://galaxy.signage.me/installplayer/"));
            //intent2.setData(Uri.parse("https://dev.signage.me/installplayer/"));
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
                case MESSAGE_TOAST:
                    Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public class WatchdogThread extends Thread
    {
        private void updateLastTimeStamp() {
            try {
                UsageStatsManager usm = (UsageStatsManager)mContext.getSystemService("usagestats");
                long currentTime = System.currentTimeMillis();
                Map<String, UsageStats> appMap = usm.queryAndAggregateUsageStats(0, currentTime);
                // UsageStats usageStats = appMap.get("com.sec.android.app.sbrowser");   // S21
                UsageStats usageStats = appMap.get("com.android.chrome");
                if (usageStats!=null) {
                    //time1 = (currentTime - usageStats.getLastTimeStamp()) / 1000;
                    time1 = (currentTime - usageStats.getLastTimeUsed()) / 1000;
                }
            } catch (Exception e) {
                Log.e("Watchdog", e.getMessage());
            }
        }

        public void run()
        {
            try {
                Log.d("Watchdog", "Thread started");
                while (true) {
                    if (watchdogEnabled) {
                        sleep(1000);
                        count++;
                        Log.d("Watchdog", "count=" + String.valueOf(count));
                        if (count > 60) {
                            Log.d("Watchdog", "no pings!!!");
                            mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                            sleep(10000);
                        }
                    }

                    if (checkLastTimeStamp) {
                        Log.d("Watchdog", "time1="+String.valueOf(time1)+" time2="+String.valueOf(time2));
                        if (time1 < time2) {
                            mMessageHandler.sendEmptyMessage(MESSAGE_RESTART_PLAYER);
                        }
                        time2 = time1;
                        sleep(5000);
                        updateLastTimeStamp();
                    }
                }
            } catch (Exception e) {

            }
            Log.d("Watchdog", "Thread end");
        }
    }
}