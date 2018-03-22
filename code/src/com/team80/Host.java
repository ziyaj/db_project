package com.team80;

import javax.swing.*;

public class Host {
    private static PersistenceLayer persistenceLayer;

    private JTextField textField1;
    private JButton submitButton;
    public JPanel HostMain;

    public Host() {
        JFrame host = new JFrame("Host");
        host.setContentPane(new Host().HostMain);
        host.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        host.pack();
        host.setVisible(true);
        persistenceLayer = PersistenceLayer.getInstance();
    }

    public static void main(String[] args) {
        JFrame host = new JFrame("Host");
        host.setContentPane(new Host().HostMain);
        host.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        host.pack();
        host.setVisible(true);
        persistenceLayer = PersistenceLayer.getInstance();
    }
}
