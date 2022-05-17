package ru.geekbrains.jt.chat.client;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class RegisterModal extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField login;
    private JTextField nickname;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;

    private String[] registerInfo = new String[3];

    public RegisterModal() {
        setTitle("Registration on ChatServer");
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        if (!Arrays.equals(passwordField1.getPassword(), passwordField2.getPassword())){
            JOptionPane.showMessageDialog(this, "ошибка при вводе пароля", "Password error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        registerInfo[0] = login.getText();
        registerInfo[1] = String.valueOf(passwordField1.getPassword());
        registerInfo[2] = nickname.getText();

        setVisible(false);
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String[] showModal(JFrame f){
        pack();
        setLocationRelativeTo(f);
        setVisible(true);
        return registerInfo;
    }

//    public static void main(String[] args) {
//        RegisterModal dialog = new RegisterModal();
//        String[] a = dialog.showModal();
//        System.out.println(Arrays.toString(a));
//        System.exit(0);
//    }
}
