package ru.geekbrains.jt.chat.core;

import ru.geekbrains.jt.common.Messages;
import ru.geekbrains.jt.network.SocketThread;
import ru.geekbrains.jt.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {
    private String nickname;
    private boolean isAuthorized;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Messages.getAuthAccept());
    }

    void authFail(){
        sendMessage(Messages.getAuthDeny());
        close();
    }

    void  msgFormatError(String msg) {
        sendMessage(Messages.getMsgFormatError(msg));
    }
}
