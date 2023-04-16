package com.sign.watchdog;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WatchdogWebSocket extends WebSocketServer {

    public WatchdogWebSocket() {
        super(new InetSocketAddress(5555));
        Log.d("WebSocketServer", "WatchdogWebSocket");
        setReuseAddr(true);
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d("WebSocketServer", "onOpen");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d("WebSocketServer", "onClose");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d("WebSocketServer", "onMessage");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d("WebSocketServer", "onError " + ex.getMessage());
    }

    @Override
    public void onStart() {
        Log.d("WebSocketServer", "onStart");
    }
}