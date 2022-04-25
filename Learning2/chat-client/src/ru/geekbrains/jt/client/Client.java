package ru.geekbrains.lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;

public class Client extends JFrame implements ActionListener, KeyListener, Thread.UncaughtExceptionHandler {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();

    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("80");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("ivan_igorevich");
    private final JPasswordField tfPassword = new JPasswordField("123456");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("Disconnect");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private final JList<String> userList = new JList<>();

    private final String logFilePath = "log_file";

    private Client() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //посреди экрана
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");
        log.setEditable(false);

        JScrollPane spLog = new JScrollPane(log);
        JScrollPane spUsers = new JScrollPane(userList);
        String[] users = {"user1", "user2",
                "user3", "user4", "user5", "user6",
                "user7", "user8", "user9",
                "user10_with_a_exceptionally_long_nickname",};
        userList.setListData(users);
        spUsers.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this);

        btnSend.addActionListener(this);
        tfMessage.addKeyListener(this);
        readLogFile();

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);

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
        } else if (src == btnSend) {
            sendMessage();
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

    //    Отправка сообщений в лог
    private void sendMessage() {
        String message = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) +" username:\n    "+ tfMessage.getText() + "\n";
        tfMessage.setText("");
        log.append(message + "\n");
        writeToFile(message);
    }


    /* начало обработка нажатий клавиш */
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Object src = e.getSource();
        if (src == tfMessage) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                sendMessage();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    /*Конец обработка нажатий клавиш*/


    /*При инициализации проверяет существование лог-файла, при существовании считывает все в лог,
    * при отсутствии выдает информационное окно. Новый файл будет создан при отправке сообщения.
    * выбор пути файла не реализован, используется путь по умолчанию.*/

    private void readLogFile(){
        try (FileInputStream f = new FileInputStream(logFilePath)) {
            InputStreamReader isr = new InputStreamReader(f);
            BufferedReader bf = new BufferedReader(isr);
            while (true) {
                String s = bf.readLine();
                if (s == null || s.equals("")) {
                    break;
                }
                log.append(s + "\n");
            }

        } catch (IOException e) {
            String msg = "Файла лога не существует, будет создан новый\n";
            JOptionPane.showMessageDialog(null,msg,"Warning",JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void writeToFile(String msg){
        try(FileOutputStream f = new FileOutputStream(logFilePath,true)) {
            byte[] buffer = msg.getBytes(StandardCharsets.UTF_8);
            f.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл лога");
        }
    }

}
