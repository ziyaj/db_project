package com.team80;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// before starting the app, you should ssh to your ugrad account
// start sqlplus, and log in with your own account
// then run "start project.sql" to make sure our data is stored into the database
public class App {
    private JButton button_msg;
    private JPanel panelMain;
    private JPasswordField passwordField1;
    private JTabbedPane tabbedPane1;
    private JPasswordField host_password;
    private JTextField host_username;
    private JTextField textField2;
    private JButton submitButton;
    private JPasswordField passwordField3;
    private JButton submitButton2;
    private JTextField textField3;
    private static PersistenceLayer persistenceLayer;

    public App() {

        button_msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "304 project initial commit");
                SQLUtil.connectOracle();
                persistenceLayer.closeConnection(); // actually this should be called when we close the app,
                // but there is not such button yet
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = host_username.getText();
                String password = host_password.getText();
                if (username.equals("leo") && password.equals("sb")) {
                    JOptionPane.showMessageDialog(null, "Login successfully");
                    JFrame host = new JFrame("host");
                    host.setVisible(true);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        persistenceLayer = PersistenceLayer.getInstance();
    }

}
