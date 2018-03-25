package com.team80;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

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

    public static ResultSet findAllPosts(int id) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            String stmt = (id == -1) ? "SELECT * FROM Posting" : "SELECT * FROM Posting WHERE hostid = ? ORDER BY pid DESC";
            PreparedStatement ps = con.prepareStatement(stmt);
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static int[] deletePosts(int[] selection, DefaultTableModel model) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM Posting WHERE pid = ?");
            for (int i = 0; i < selection.length; i++) {
                int pid = (int)model.getValueAt(selection[i], 0);
                ps.setInt(1, pid);
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static int updatePost(int[] selection, DefaultTableModel model, Date fromDate, Date toDate, String description) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();

            Date fDate = (fromDate == null) ? (Date) model.getValueAt(selection[0], 1) : fromDate;
            Date tDate = (toDate == null) ? (Date) model.getValueAt(selection[0], 2) : toDate;

            if(fDate.after(tDate)){
                return 0;
            }

            String stmt = "UPDATE Posting SET description = ? , fromdate = ? , todate = ? WHERE pid = ?";
            PreparedStatement ps = con.prepareStatement(stmt);
            int pid = (int)model.getValueAt(selection[0], 0);
            ps.setString(1, description);
            ps.setDate(2, fDate);
            ps.setDate(3, tDate);
            ps.setInt(4, pid);
            return ps.executeUpdate();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public static int addPost(int id, Date fromDate, Date toDate, String description) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(pid) FROM Posting");
            ResultSet rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            System.out.println("count: " + count);
            ps = con.prepareStatement("INSERT INTO Posting VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, count + 1);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ps.setString(4, description);
            ps.setInt(5, id);
            return ps.executeUpdate();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public static ResultSet getHost(int id) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Hosts WHERE cid = ?");
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static int addHost(int cid, boolean isChecked, String roomNo, String residence, int dailyRate) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Hosts VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, cid);
            ps.setBoolean(2, isChecked);
            ps.setString(3, roomNo);
            ps.setString(4, residence);
            ps.setInt(5, dailyRate);
            return ps.executeUpdate();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public static ResultSet getStudent(int id) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Student WHERE cid = ?");
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
