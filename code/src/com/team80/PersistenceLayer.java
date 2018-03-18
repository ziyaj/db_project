package com.team80;

import java.sql.*;

public class PersistenceLayer {

    public static void connectOracle() {
        System.out.println("connecting oracle...");
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1522:ug", "ora_f4a0b", "a34893140");
            System.out.println("connection created");
            Statement stmt = con.createStatement();
            // stmt is a statement object
            ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
            while (rs.next()) {
                System.out.println("cid: " + rs.getInt(1));
                System.out.println("cname: " + rs.getString(2));
                System.out.println("gender: " + rs.getString(3));
                System.out.println("credit: " + rs.getInt(4));
                System.out.println("is_active: " + rs.getInt(5));
            }
            con.close();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
