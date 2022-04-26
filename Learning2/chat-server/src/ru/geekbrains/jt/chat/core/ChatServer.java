package ru.geekbrains.jt.chat.core;

import ru.geekbrains.jt.network.ServerSocketThread;
import ru.geekbrains.jt.network.ServerSocketThreadListener;
import ru.geekbrains.jt.network.SocketThread;
import ru.geekbrains.jt.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    private final int SERVER_SOCKET_TIMEOUT = 2000;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final Vector<SocketThread> clients = new Vector<>();

    int counter = 0;
    ServerSocketThread server;

    public void start(int port) {
        if (server != null && server.isAlive()) {
            System.out.println("Server already started");
        } else {
            server = new ServerSocketThread(this, "Chat server " + counter++, port, SERVER_SOCKET_TIMEOUT);
        }
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            System.out.println("Server is not running");
        } else {
            server.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() +
                ": " + msg;
        System.out.println(msg);
    }

    /**
     * Server socket thread methods
     * */

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread t, ServerSocket s) {
        putLog("Server socket created");
    }

    @Override
    public void onServerSoTimeout(ServerSocketThread t, ServerSocket s) {
        //
    }

    @Override
    public void onSocketAccepted(ServerSocketThread t, ServerSocket s, Socket client) {
        putLog("client connected");
        String name = "SocketThread" + client.getInetAddress() + ": " + client.getPort();
        clients.add(new SocketThread(this, name, client));
    }

    @Override
    public void onServerException(ServerSocketThread t, Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onSocketStart(SocketThread t, Socket s) {
        putLog("Client connected");
    }

    @Override
    public void onSocketStop(SocketThread t) {
        putLog("client disconnected");
    }

    @Override
    public void onSocketReady(SocketThread t, Socket socket) {
        putLog("client is ready");
    }

    @Override
    public void onReceiveString(SocketThread t, Socket s, String msg) {
        t.sendMessage("echo: " + msg);
    }

    @Override
    public void onSocketException(SocketThread t, Throwable e) {
        e.printStackTrace();
    }

    public void sendMessageToAll(String msg){
        clients.forEach(socketThread -> socketThread.sendMessage(msg));
    }
}
