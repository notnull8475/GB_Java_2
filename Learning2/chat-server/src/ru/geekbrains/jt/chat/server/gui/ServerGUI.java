package ru.geekbrains.jt.chat.server.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.jt.chat.server.core.ChatServer;
import ru.geekbrains.jt.chat.server.core.ChatServerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ChatServerListener {
    private static final Logger log = LogManager.getLogger(ServerGUI.class);

    private static final int POS_X = 800;
    private static final int POS_Y = 200;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final ChatServer server = new ChatServer(this);
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JPanel panelTop = new JPanel(new GridLayout(1, 2));
    private final JTextArea textArea = new JTextArea();

    private ServerGUI() {
        log.trace("Server Gui constructor start");
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(textArea);
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        panelTop.add(btnStart);
        panelTop.add(btnStop);
        add(panelTop, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        setVisible(true);
        log.trace("ServerGui constructor finish");
    }

    public static void main(String[] args) {
        log.debug(" ServerGui thread start ");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("Action listener. RECEIVED VARIABLE  = " + e.toString());
        Object src = e.getSource();
        if (src == btnStart) {
            server.start(8189);
        } else if (src == btnStop) {
            server.stop();
        } else {
            log.error("ACTION FOR COMPONENT UNIMPLEMENTED");
            throw new RuntimeException("Action for component unimplemented");
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("THREAD NAME: " + t.getName() + " ERROR MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onChatServerMessage(String msg) {
        log.debug("CHAT SERVER MESSAGE: " + msg);
        SwingUtilities.invokeLater(() -> {
            textArea.append(msg + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}
