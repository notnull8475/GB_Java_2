package ru.geekbrains.jt.chat.client;

import javax.swing.*;
import java.awt.event.*;

public class UpdateUserModal extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField login;
    private JTextField newNick;

    private String[] updateInfo = new String[2];

    public UpdateUserModal() {
        setTitle("Nickname update");
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
        updateInfo[0] = login.getText();
        updateInfo[1] = newNick.getText();// add your code here
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String[] showModal(JFrame f) {
        pack();
        setLocationRelativeTo(f);
        setVisible(true);
        return updateInfo;
    }

//    public static void main(String[] args) {
//        UpdateUserModal dialog = new UpdateUserModal();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
