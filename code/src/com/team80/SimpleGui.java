/*

Use cardLayout to enable switch between different panel

 */


package com.team80;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class SimpleGui extends JFrame {

    private JPanel pane = null; // JPanel layout will be set to CardLayout
    private JPanel p = null;    // JPanel for botton
    private CardLayout card = null; // CardLayout

    private JButton b_traveller = null, b_host = null, b_admin = null; // button
    private JPanel p_login=null, p_traveller = null, p_host = null, p_admin = null; //  JPanels for traveller, host, admin

    public SimpleGui() {

        card = new CardLayout(5, 5);
        pane = new JPanel(card); // CardLayout
        p = new JPanel();        // JPanel for button

        b_traveller = new JButton("Traveller");
        b_host = new JButton("Host");
        b_admin = new JButton("Admin");

        b_traveller.setMargin(new Insets(2,2,2,2));
        b_host.setMargin(new Insets(2,2,2,2));
        b_admin.setMargin(new Insets(2,2,2,2));


        p.add(b_traveller);
        p.add(b_host);
        p.add(b_admin);

        p_login = new JPanel();
        p_traveller = new JPanel();
        p_host = new JPanel();
        p_admin = new JPanel();

        //test switch functionality
        p_login.setBackground(Color.RED);
        p_traveller.setBackground(Color.WHITE);
        p_host.setBackground(Color.BLUE);
        p_admin.setBackground(Color.GREEN);

        p_login.add(new JLabel("JPanel_Login"));
        p_traveller.add(new JLabel("JPanel_Traveller"));
        p_host.add(new JLabel("JPanel_Host"));
        p_admin.add(new JLabel("JPanel_Admin"));

        pane.add(p_login, "p0");
        pane.add(p_traveller, "p1");
        pane.add(p_host, "p2");
        pane.add(p_admin, "p3");


        b_traveller.addActionListener(new ActionListener() { // 直接翻转到p_1
            public void actionPerformed(ActionEvent e) {
                card.show(pane, "p1");
            }
        });
        b_host.addActionListener(new ActionListener() { // 直接翻转到p_2
            public void actionPerformed(ActionEvent e) {
                card.show(pane, "p2");
            }
        });
        b_admin.addActionListener(new ActionListener() { // 直接翻转到p_3
            public void actionPerformed(ActionEvent e) {
                card.show(pane, "p3");
            }
        });

        this.getContentPane().add(pane);
        this.getContentPane().add(p, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleGui();
    }

}
