package ru.geekbrains.jt.network;

import java.net.Socket;

public interface SocketThreadListener {
    void onSocketStart();
    void onSocketStop(SocketThread t);

    void onSocketReady(SocketThread t, Socket socket);
    void onReceiveString(SocketThread t, Socket s, String msg);

    void onSocketException(SocketThread t, Throwable e);
}
