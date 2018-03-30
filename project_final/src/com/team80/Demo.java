package com.team80;

import javax.swing.*;

// ********************Before testing, make sure you are NOT currently logged in SQL using SSH *********************
public class Demo {

    public static void main(String[] args) {

        JFrame frame = new JFrame("App");
        App app = new App();

        frame.setContentPane(app.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
