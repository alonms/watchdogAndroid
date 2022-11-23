package com.sign.watchdog;

import android.content.Context;
import android.widget.Toast;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;





public class WebsocketServer extends WebSocketServer
{
    private Context context;
    public WebsocketServer(Context context, InetSocketAddress address) {
        super(address);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
        // TODO Auto-generated method stub
        System.out.println("onClose!!!!!!!!!!");
    }

    @Override
    public void onError(WebSocket arg0, Exception arg1) {
        // TODO Auto-generated method stub
        System.out.println(arg1.getStackTrace());

    }

    @Override
    public void onMessage(WebSocket arg0, String arg1) {
        // TODO Auto-generated method stub
        System.out.println("onMessage!!!!!!!!!! " + arg1);
        arg0.send("from android");
        arg0.close();
    }

    @Override
    public void onOpen(WebSocket arg0, ClientHandshake arg1) {
        // TODO Auto-generated method stub

        System.out.println("new connection to " + arg0.getRemoteSocketAddress());


    }
}