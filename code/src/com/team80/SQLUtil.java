package com.team80;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtil {

    // a sample method that executes a single query and prints results
    public static void connectOracle() {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
            System.out.println();
            System.out.println("Received data from server: ");
            while (rs.next()) {
                System.out.println("cid: " + rs.getInt(1));
                System.out.println("cname: " + rs.getString(2));
                System.out.println("gender: " + rs.getString(3));
                System.out.println("credit: " + rs.getInt(4));
                System.out.println("is_active: " + rs.getInt(5));
            }
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
    }

    public static ResultSet findAllPosts() {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT * FROM Posting");
            return rs;
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

}
