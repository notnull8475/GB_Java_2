package ru.geekbrains.jt.chat.gui;

import ru.geekbrains.jt.chat.core.ChatServer;
import ru.geekbrains.jt.chat.core.ChatServerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ChatServerListener {
    private static final int POS_X = 100;
    private static final int POS_Y = 100;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private final ChatServer server = new ChatServer(this);
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JPanel panelTop = new JPanel(new GridLayout(1,2));
    private final JTextArea log = new JTextArea();

    private ServerGUI() {
        setDefaultLookAndFeelDecorated(true);
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        setLayout(new GridLayout(2, 1));
        JScrollPane scrollLog = new JScrollPane(log);
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        panelTop.add(btnStart);
        panelTop.add(btnStop);
        add(panelTop,BorderLayout.NORTH);
        add(scrollLog,BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("main started");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
        System.out.println("main ended");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStart) {
            server.start(8189);
        } else if (src == btnStop) {
            server.stop();
        } else {
            throw new RuntimeException("Action for component unimplemented");
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg = "Exception in thread " + t.getName() +
                " " + e.getClass().getCanonicalName() +
                ": " + e.getMessage() +
                "\n\t" + e.getStackTrace()[0];
        JOptionPane.showMessageDialog(null, msg,
                "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onChatServerMessage(String msg) {
        SwingUtilities.invokeLater(()->{
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
