package ru.geekbrains.jt.chat.server.core;

import ru.geekbrains.jt.chat.common.Messages;
import ru.geekbrains.jt.chat.server.dbWork.DBUtils;
import ru.geekbrains.jt.chat.server.dbWork.SqlClient;
import ru.geekbrains.jt.network.ServerSocketThread;
import ru.geekbrains.jt.network.ServerSocketThreadListener;
import ru.geekbrains.jt.network.SocketThread;
import ru.geekbrains.jt.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    private Connection conn;

    private final int SERVER_SOCKET_TIMEOUT = 2000;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final Vector<SocketThread> clients = new Vector<>();

    private static final long TIMEOUT = 10_000;
//    private static final long TIMEOUT = 120_000;

    int counter = 0;
    ServerSocketThread server;
    final ChatServerListener listener;

    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
    }

    public void start(int port) {
        if (server != null && server.isAlive()) {
            putLog("Server already started");
        } else {
            server = new ServerSocketThread(this, "Chat server " + counter++, port, SERVER_SOCKET_TIMEOUT);
        }
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            putLog("Server is not running");
        } else {
            server.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() +
                ": " + msg;
        listener.onChatServerMessage(msg);
    }

    /**
     * Server socket thread methods
     */

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
        try {
            conn = SqlClient.connect();
        } catch (ClassNotFoundException | SQLException ef) {
            throw new RuntimeException(ef);
        }
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
        SqlClient.disconnect(conn);
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).close();
        }
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
        new ClientThread(this, name, client);
    }

    @Override
    public void onServerException(ServerSocketThread t, Throwable e) {
        e.printStackTrace();
    }

    /**
     * Socket Thread listening
     */

    @Override
    public synchronized void onSocketStart(SocketThread t, Socket s) {
        putLog("Client connected");
    }

    @Override
    public synchronized void onSocketStop(SocketThread t) {
        ClientThread client = (ClientThread) t;
        clients.remove(client);
        if (client.isAuthorized() && !client.isReconnecting()) {
            sendToAllAuthorized(Messages.getTypeBroadcast("Server", client.getNickname() + " disconnected"));
        }
        sendToAllAuthorized(Messages.getUserList(getUsers()));
    }

    @Override
    public synchronized void onSocketReady(SocketThread t, Socket socket) {
        putLog("client is ready");
        clients.add(t);
    }

    @Override
    public synchronized void onReceiveString(SocketThread t, Socket s, String msg) {
        ClientThread client = (ClientThread) t;
        if (client.isAuthorized()) {
            handleAuthMsg(client, msg);
        } else {
            if (System.currentTimeMillis() > client.getConnectTime() + TIMEOUT) {
                client.close();
            }
            handleNonAuthMsg(client, msg);
        }
    }

    private void handleAuthMsg(ClientThread client, String msg) {
        String[] arr = msg.split(Messages.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Messages.USER_BROADCAST ->
                    sendToAllAuthorized(Messages.getTypeBroadcast(client.getNickname(), arr[1]));
            case Messages.USER_NICK_UPDATE -> {
                putLog(Arrays.toString(arr));
                updateNick(client, arr);
            }
            default -> client.msgFormatError(msg);
        }

    }

    private void sendToAllAuthorized(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            client.sendMessage(msg);
        }
    }

    private void handleNonAuthMsg(ClientThread client, String msg) {
        String[] arr = msg.split(Messages.DELIMITER);
        if (arr.length == 3 && arr[0].equals(Messages.AUTH_REQUEST)) {
            authClient(client, arr);
        } else if (arr.length == 4 && arr[0].equals(Messages.USER_REGISTER)) {
            registrationRequest(client, arr);
        } else {
            client.msgFormatError(msg);
        }
    }

    private void registrationRequest(ClientThread client, String[] arr) {
        try {
            if(DBUtils.registerUser(conn, arr[1], arr[2], arr[3])>0){
                client.authAccept(arr[3]);
                String msg = "Клиент " + client.getNickname() + "   зарегистрировался";
                sendToAllAuthorized(Messages.getTypeBroadcast("Server", msg));
                sendToAllAuthorized(Messages.getUserList(getUsers()));
            } else {
                client.msgFormatError("Ошибка регистрации, не удалось добавить пользователя ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            client.msgFormatError("Ошибка регистрации " + e.getErrorCode());
            putLog(e.getMessage());
        }
    }

    private void updateNick(ClientThread client, String[] arr) {
        try {
            int flag = DBUtils.updateUserNick(conn, arr[1], arr[2]);
            if (flag > 0) {
                String msg = client.getNickname() + " сменил ник на " + arr[2];
                sendToAllAuthorized(Messages.getTypeBroadcast("Server", msg));
                client.setNickname(arr[2]);
                sendToAllAuthorized(Messages.getUserList(getUsers()));
            } else {
                client.msgFormatError("ошибка смены ника");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void authClient(ClientThread client, String[] arr) {
        String login = arr[1];
        String password = arr[2];
        String nickname = null;
        try {
            nickname = DBUtils.getNick(conn, login, password);
        } catch (SQLException e) {
            putLog(e.getSQLState());
        }
        if (nickname == null) {
            putLog("Invalid login attempt " + login);
            client.authFail();
            return;
        } else {
            ClientThread oldClient = findClientByNickname(nickname);
            client.authAccept(nickname);
            if (oldClient == null) {
                sendToAllAuthorized(Messages.getTypeBroadcast("Server", nickname + " connected."));
            } else {
                oldClient.reconnect();
                clients.remove(oldClient);
            }
        }
        sendToAllAuthorized(Messages.getUserList(getUsers()));
    }

    @Override
    public synchronized void onSocketException(SocketThread t, Throwable e) {
        e.printStackTrace();
    }

    private String getUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            sb.append(client.getNickname()).append(Messages.DELIMITER);
        }
        return sb.toString();
    }

    private synchronized ClientThread findClientByNickname(String nickname) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            if (client.getNickname().equals(nickname))
                return client;
        }
        return null;
    }
}
