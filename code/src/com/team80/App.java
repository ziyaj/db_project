package com.team80;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// before starting the app, you should ssh to your ugrad account
// start sqlplus, and log in with your own account
// then run "start project.sql" to make sure our data is stored into the database
public class App {
    private JPanel loginPanel;
    private JButton button1;
    private JButton button2;
    private static PersistenceLayer persistenceLayer;

    public App() {
        button2 = new JButton();
        button2.setBounds(10, 10, 10, 10 );
        button1 = new JButton();
        button1.setBounds(10, 10, 10, 10 );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        init();
    }

    public static void init() {
        persistenceLayer = PersistenceLayer.getInstance();
        SQLUtil.connectOracle();
        persistenceLayer.closeConnection(); // actually this should be called when we close the app,
        // but there is not such button yet
    }

}
