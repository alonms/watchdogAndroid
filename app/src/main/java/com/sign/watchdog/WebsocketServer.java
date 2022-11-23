package com.sign.watchdog;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;





public class WebsocketServer extends WebSocketServer
{

    public WebsocketServer(InetSocketAddress address) {
        super(address);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(WebSocket arg0, Exception arg1) {
        // TODO Auto-generated method stub
        System.out.println(arg1.getStackTrace());

    }

    @Override
    public void onMessage(WebSocket arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOpen(WebSocket arg0, ClientHandshake arg1) {
        // TODO Auto-generated method stub

        System.out.println("new connection to " + arg0.getRemoteSocketAddress());


    }
}