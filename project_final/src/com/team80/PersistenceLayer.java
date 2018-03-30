package com.team80;

import java.sql.*;

public class PersistenceLayer {

    private static PersistenceLayer instance = null; // this is a singleton class

    private Connection con;

    // the connection is created upon initializing the PersistenceLayer singleton object
    //
    // below is the command to open an ssh tunnel, you can use your own cs account
    // ssh -l f4a0b -N -L localhost:1522:dbhost.ugrad.cs.ubc.ca:1522 valdes.ugrad.cs.ubc.ca
    // git stash save PersistenceLayer.java
    // git pull
    // git stash pop
    private PersistenceLayer() {
        System.out.println();
        System.out.println("Connecting to Oracle...");
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            this.con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1522:ug",
                    "ora_f4a0b",      // don't forget to change this if you use your account
                    "a34893140"); // don't forget to change this if you use your account
            System.out.println("Connection created.");
        } catch (final SQLException e) {
            System.err.println("An error occurred while creating connection to Oracle server.");
            System.err.println(e.getMessage());
        }
    }

    public static PersistenceLayer getInstance() {
        if (instance == null) {
            instance = new PersistenceLayer();
        }
        return instance;
    }

    // remember to call closeConnection() when the app quits.
    public void closeConnection() {
        try {
            con.close();
            instance = null;
            System.out.println();
            System.out.println("Connection closed.");
        } catch (final SQLException e) {
            System.err.println("An error occurred while closing connection.");
            System.err.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return con;
    }

}
