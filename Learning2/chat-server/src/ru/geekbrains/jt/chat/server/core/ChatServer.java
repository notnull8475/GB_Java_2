package ru.geekbrains.jt.chat.server.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.jt.chat.common.Messages;
import ru.geekbrains.jt.chat.server.dbWork.DBUtils;
import ru.geekbrains.jt.chat.server.dbWork.SqlClient;
import ru.geekbrains.jt.chat.server.utils.LimitedQueue;
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
    private static final Logger log = LogManager.getLogger(ChatServer.class);
    private Connection conn;
    private final int SERVER_SOCKET_TIMEOUT = 2000;
    private final int MSG_NUMB = 100;
    private LimitedQueue<String> chatHistory;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final Vector<SocketThread> clients = new Vector<>();


    private DBUtils dbUtils;

    private static final long TIMEOUT = 10_000;
//    private static final long TIMEOUT = 120_000;

    int counter = 0;
    ServerSocketThread server;
    final ChatServerListener listener;

    public ChatServer(ChatServerListener listener) {
        log.debug("CHAT SERVER INIT, LISTENER - " + listener.toString());
        this.listener = listener;
        dbUtils = new DBUtils();
        chatHistory = new LimitedQueue<>(MSG_NUMB);
    }

    public void start(int port) {
        log.trace("SERVER SOCKET THREAD TRY START");
        if (server != null && server.isAlive()) {
            log.trace("SERVER ALREADY STARTED");
        } else {
            log.debug("SERVER SOCKET THREAD START: PORT=" + port + " SERVER_SOCKET_TIMEOUT=" + SERVER_SOCKET_TIMEOUT);
            server = new ServerSocketThread(this, "Chat server " + counter++, port, SERVER_SOCKET_TIMEOUT);
        }
    }

    public void stop() {
        log.info("SERVER SOCKET THREAD TRY STOP");
        if (server == null || !server.isAlive()) {
            log.info("SERVER IS NOT RUNNING");
        } else {
            server.interrupt();
        }
    }

//    private void log.debug(String msg) {
//        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
//                Thread.currentThread().getName() +
//                ": " + msg;
//        listener.onChatServerMessage(msg);
//    }

    /**
     * Server socket thread methods
     */

    @Override
    public void onServerStart(ServerSocketThread thread) {
        log.trace("SERVER THREAD STARTED");
        try {
            conn = SqlClient.connect();
        } catch (ClassNotFoundException | SQLException ef) {
            log.error("SQL CONNECTION ERROR\n" + ef.getMessage());
            throw new RuntimeException(ef);
        }
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        log.trace("SERVER THREAD STOPPED");
        SqlClient.disconnect(conn);
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).close();
        }
//        clients.shutdownNow();
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread t, ServerSocket s) {
        log.info("SERVER SOCKET CREATED " + t.getName() + " SERVER_SOCKET" + s.toString());
    }

    @Override
    public void onServerSoTimeout(ServerSocketThread t, ServerSocket s) {
        log.info("SERVET TIMEOUT: " + t.getName() + "\nSERVER_SOCKET " + s.toString());
        //
    }

    @Override
    public void onSocketAccepted(ServerSocketThread t, ServerSocket s, Socket client) {
        log.trace("CLIENT CONNECTED");
        String name = "SocketThread" + client.getInetAddress() + ": " + client.getPort();
        log.trace(name);
        new ClientThread(this, name, client);
    }

    @Override
    public void onServerException(ServerSocketThread t, Throwable e) {
        log.error(e.getMessage());
        e.printStackTrace();
    }

    /**
     * Socket Thread listening
     */

    @Override
    public synchronized void onSocketStart(SocketThread t, Socket s) {
        log.trace("CLIENT CONNECTED");
    }

    @Override
    public synchronized void onSocketStop(SocketThread t) {
        log.trace(t.getName() + " SOCKET STOP");
        ClientThread client = (ClientThread) t;
        if (client.isAuthorized() && !client.isReconnecting()) {
            sendToAllAuthorized(Messages.getTypeBroadcast("Server", client.getNickname() + " disconnected"));
        }
        sendToAllAuthorized(Messages.getUserList(getUsers()));
    }

    @Override
    public synchronized void onSocketReady(SocketThread t, Socket socket) {
        log.trace("client is ready");
//        clients.add(t);
    }

    @Override
    public synchronized void onReceiveString(SocketThread t, Socket s, String msg) {
        log.trace("RECEIVE ACTION\n" + t.getName() + " " + s.toString() + " \nMESSAGE" + msg);
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
            case Messages.USER_BROADCAST -> {
                String m = Messages.getTypeBroadcast(client.getNickname(), arr[1]);
                sendToAllAuthorized(m);
                chatHistory.add(m);
            }
            case Messages.USER_NICK_UPDATE -> {
                log.info(Arrays.toString(arr));
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
            if (dbUtils.registerUser(conn, arr[1], arr[2], arr[3]) > 0) {
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
            log.debug(e.getMessage());
        }
    }

    private void updateNick(ClientThread client, String[] arr) {
        try {
            int flag = dbUtils.updateUserNick(conn, arr[1], arr[2]);
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
            nickname = dbUtils.getNick(conn, login, password);
        } catch (SQLException e) {
            log.error(e.getSQLState());
        }
        if (nickname == null) {
            log.info("Invalid login attempt " + login);
            client.authFail();
            return;
        } else {
            ClientThread oldClient = findClientByNickname(nickname);
            client.authAccept(nickname);
            if (oldClient == null) {
                String msg = Messages.getTypeBroadcast("Server", nickname + " connected.");
                sendToAllAuthorized(msg);
                sendHistory(client);
            } else {
                oldClient.reconnect();
                sendHistory(oldClient);
                clients.remove(oldClient);
            }
        }
        sendToAllAuthorized(Messages.getUserList(getUsers()));
    }

    @Override
    public synchronized void onSocketException(SocketThread t, Throwable e) {
        log.error(e.getMessage());
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

    private synchronized void sendHistory(ClientThread c) {
        log.trace("SEND HISTORY");
        for (String s : chatHistory) {
            c.sendMessage(s);
        }
    }
}
