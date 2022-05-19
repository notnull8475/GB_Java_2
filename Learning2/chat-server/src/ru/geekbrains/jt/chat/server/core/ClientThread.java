package ru.geekbrains.jt.chat.server.core;

import ru.geekbrains.jt.chat.common.Messages;
import ru.geekbrains.jt.network.SocketThread;
import ru.geekbrains.jt.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {
    private String nickname;
    private boolean isAuthorized;
    private boolean isReconnecting;

    private final long connectTime;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
        connectTime = System.currentTimeMillis();
    }

    public boolean isReconnecting() {
        return isReconnecting;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void reconnect() {
        isReconnecting = true;
        close();
    }

    void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Messages.getAuthAccept(nickname));
    }

    void authFail() {
        sendMessage(Messages.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(Messages.getMsgFormatError(msg));
        close();
    }

    public long getConnectTime() {
        return connectTime;
    }
}
