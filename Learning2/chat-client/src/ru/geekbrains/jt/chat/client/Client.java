package ru.geekbrains.jt.chat.client;

import ru.geekbrains.jt.chat.common.Messages;
import ru.geekbrains.jt.network.SocketThread;
import ru.geekbrains.jt.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class Client extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private static final int POS_X = 400;
    private static final int POS_Y = 200;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final String TITLE = "Chat Client";
    private final JTextArea log = new JTextArea();

    private final JPanel panelTop = new JPanel(new GridLayout(2, 4));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JCheckBox cbResizable = new JCheckBox("Resizable");
    private final JTextField tfLogin = new JTextField("notnull");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnRegister = new JButton("SignUp");
    private final JButton btnUpdateInfo = new JButton("Edit");

    private final JPanel panelBottom = new JPanel(new GridLayout(1, 4));
    private final JButton btnDisconnect = new JButton("Disconnect");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("<html><b>Send</b></html>");
    private final JList<String> userList = new JList<>();

    private boolean shownIoErrors = false;
    private SocketThread socketThread;

    private boolean socketReady = false;
    private static final long TIMEOUT = 10_000;

    private String nickname;

    private Client() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //посреди экрана
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setTitle(TITLE);
        setResizable(false);
        setAlwaysOnTop(true);
        cbAlwaysOnTop.setSelected(true);
        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane spLog = new JScrollPane(log);
        JScrollPane spUsers = new JScrollPane(userList);
        spUsers.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addActionListener(this);
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
        btnDisconnect.addActionListener(this);
        btnUpdateInfo.addActionListener(this);

        panelBottom.setVisible(false);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(cbResizable);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelTop.add(btnRegister);
        panelBottom.add(btnDisconnect);
        panelBottom.add(tfMessage);
        panelBottom.add(btnSend);
        panelBottom.add(btnUpdateInfo);

        add(panelBottom, BorderLayout.SOUTH);
        add(panelTop, BorderLayout.NORTH);
        add(spLog, BorderLayout.CENTER);
        add(spUsers, BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == cbResizable) {
            setResizable(!cbResizable.isSelected());
        } else if (src == btnSend || src == tfMessage) {
            sendMessage();
        } else if (src == btnLogin) {
            connect();
            long time = System.currentTimeMillis() + TIMEOUT;
            while (System.currentTimeMillis() < time) {
                if (socketReady) {
                    logIn(socketThread);
                    break;
                }
            }
        } else if (src == btnRegister) {
            System.out.println("register modal window");
            RegisterModal dialog = new RegisterModal();
            String[] arr = dialog.showModal(this);
            connect();
            long time = System.currentTimeMillis() + TIMEOUT;
            while (System.currentTimeMillis() < time) {
                if (socketReady) {
                    register(socketThread, arr);
                    break;
                }
            }
        } else if (src == btnUpdateInfo) {
            UpdateUserModal modal = new UpdateUserModal();
            String[] arr = modal.showModal(this);
            updateUser(socketThread, arr);
        } else if (src == btnDisconnect) {
            socketThread.close();
        } else {
            throw new RuntimeException("Action for component unimplemented");
        }
    }

    private void connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException e) {
            showException(Thread.currentThread(), e);
        }
    }

    private void logIn(SocketThread t) {
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String pass = new String(tfPassword.getPassword());
        t.sendMessage(Messages.getAuthRequest(login, pass));
    }

    private void register(SocketThread t, String[] arr) {
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        t.sendMessage(Messages.getTypeRegister(arr[0], arr[1], arr[2]));
    }

    private void updateUser(SocketThread t, String[] arr) {
        t.sendMessage(Messages.getTypeNickUpdate(arr[0], arr[1]));
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.grabFocus();
        socketThread.sendMessage(Messages.getTypeBcastFromClient(msg));
    }
//codewars, hackerrank, leetcode, codegame

    private void wrtMsgToLogFile(String msg, String username) {
        try (FileWriter out = new FileWriter("history_"+username+".txt", true)) {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        //showException(t, e);
    }

    @Override
    public void onSocketStart(SocketThread t, Socket s) {
        putLog("Start");
    }

    @Override
    public void onSocketStop(SocketThread t) {
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
        setTitle(TITLE);
        userList.setListData(new String[0]);
    }

    @Override
    public void onSocketReady(SocketThread t, Socket socket) {
        putLog("socket ready");
        socketReady = true;
    }

    @Override
    public void onReceiveString(SocketThread t, Socket s, String msg) {
        handleMessage(msg);
    }

    void handleMessage(String value) {
        String[] arr = value.split(Messages.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Messages.AUTH_ACCEPT:
                nickname = arr[1];
                setTitle(TITLE + " logged in as: " + nickname);
                break;
            case Messages.AUTH_DENY:
                putLog(value);
                break;
            case Messages.MSG_FORMAT_ERROR:
                putLog("Ошибка в сообщении:  " + arr[1]);
                socketThread.close();
                break;
            case Messages.USER_LIST:
                String users = value.substring(Messages.DELIMITER.length() +
                        Messages.USER_LIST.length());
                String[] usersArr = users.split(Messages.DELIMITER);
                Arrays.sort(usersArr);
                userList.setListData(usersArr);
                break;
            case Messages.MSG_BROADCAST:
                String msg = DATE_FORMAT.format(Long.parseLong(arr[1])) + ": " + arr[2] + ": " + arr[3];
                putLog(msg);
                wrtMsgToLogFile(msg, nickname);
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msgType);
        }
    }

    @Override
    public void onSocketException(SocketThread t, Throwable e) {
        showException(t, e);
    }
}
