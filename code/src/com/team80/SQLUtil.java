package com.team80;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class SQLUtil {

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

    /**
     * H1. a host can add a post
     * @param hostid
     * @param fromDate
     * @param toDate
     * @param description
     * @return ResultSet
     */
    public static int addPost(final int hostid, final Date fromDate, final Date toDate, final String description) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final int count = getMaxPostingId();
            System.out.println("count: " + count);
            final PreparedStatement ps = con.prepareStatement("INSERT INTO Posting VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, count + 1);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ps.setString(4, description);
            ps.setInt(5, hostid);
            System.out.println("Successfully added a post!");
            return ps.executeUpdate();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * H2. a host can see all his contracts
     * @return ResultSet rs
     */
    public static ResultSet findHostsPostings(final int hostid) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT PI.pid, PI.fromdate, PI.todate, PI.roomno, PI.residencename, PI.dailyrate, PI.description " +
                    "FROM PostingInfo PI " +
                    "WHERE PI.hostid = " + hostid);
            // pid, fromdate, todate, roomno, residencename, dailyrate, description
//            while (rs.next()) {
//                System.out.println("pid: " + rs.getInt(1));
//                System.out.println("fromdate: " + rs.getString(2));
//                System.out.println("todate: " + rs.getString(3));
//                System.out.println("roomno: " + rs.getString(4));
//                System.out.println("residencename: " + rs.getString(5));
//                System.out.println("dailyrate: " + rs.getInt(6));
//                System.out.println("description: " + rs.getString(7));
//            }
            return rs;
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * H3. a host can update his post
     * @param selection
     * @param model
     * @param fromDate
     * @param toDate
     * @param description
     * @return int
     */
    public static int updatePost(int[] selection, DefaultTableModel model, Date fromDate, Date toDate, String description) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();

            final Date fDate = (fromDate == null) ? (Date) model.getValueAt(selection[0], 1) : fromDate;
            final Date tDate = (toDate == null) ? (Date) model.getValueAt(selection[0], 2) : toDate;

            if(fDate.after(tDate)){
                return 0;
            }

            final String stmt = "UPDATE Posting SET description = ? , fromdate = ? , todate = ? WHERE pid = ?";
            final PreparedStatement ps = con.prepareStatement(stmt);
            final int pid = (int)model.getValueAt(selection[0], 0);
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

    /**
     * H4. a host can see all his contracts
     * @return
     */
    public static ResultSet findHostsContracts(final int hostid) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT CS.contractid, CS.fromdate, CS.todate, CS.travelerid, S.name, U.name " +
                    "FROM Contract_Signs CS, Traveler T, Student S, University U " +
                    "WHERE CS.hostid = " + hostid + " AND T.cid = CS.travelerid AND T.cid = S.cid AND S.unid = U.unid");
            // contractid, fromdate, todate, travelerid, name, university
//            while (rs.next()) {
//                System.out.println("contractid: " + rs.getInt(1));
//                System.out.println("fromdate: " + rs.getString(2));
//                System.out.println("todate: " + rs.getString(3));
//                System.out.println("travelerid: " + rs.getInt(4));
//                System.out.println("name: " + rs.getString(5));
//                System.out.println("university: " + rs.getString(6));
//            }
            return rs;
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * H5. a host can see all the ratings the host has done
     * @param hostid
     * @return ResultSet rs
     */
    public static ResultSet findHostsReviews(final int hostid) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT HR.travelerid, S.name ,HR.rating " +
                    "FROM Host_Reviews HR, Traveler T, Student S " +
                    "WHERE HR.hostid = " + hostid + "AND HR.travelerid = T.cid AND T.cid = S.cid");
            // travelerid, name, rating
//            while (rs.next()) {
//                System.out.println("contractid: " + rs.getInt(1));
//                System.out.println("name: " + rs.getString(2));
//                System.out.println("rating: " + rs.getInt(3));
//            }
            return rs;
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * H6. a host can review a traveler
     * @param hostid
     * @param travelerid
     * @param rating
     * @return int
     */
    public static int addHostReview(final int hostid, final int travelerid, final int rating) {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final PreparedStatement ps = con.prepareStatement("INSERT INTO Host_Reviews VALUES (?, ?, ?)");
            ps.setInt(1, travelerid);
            ps.setInt(2, hostid);
            ps.setInt(3, rating);
            System.out.println("Host has added a new review to a traveler");
            return ps.executeUpdate();
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * T2 - find cheapest posts
     * @return ResultSet rs
     */
    public static ResultSet findCheapestPosts() {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT * FROM PostingInfo PI WHERE PI.dailyrate = (SELECT MIN(dailyrate) FROM PostingInfo)");
            // pid, fromdate, todate, hostid, hostname, roomno, residencename, university, dailyrate
//            while (rs.next()) {
//                System.out.println("pid: " + rs.getInt(1));
//                System.out.println("fromdate: " + rs.getString(2));
//                System.out.println("todate: " + rs.getString(3));
//                System.out.println("hostid: " + rs.getInt(4));
//                System.out.println("hostname: " + rs.getString(5));
//                System.out.println("roomno: " + rs.getString(6));
//                System.out.println("residencename: " + rs.getString(7));
//                System.out.println("university: " + rs.getString(8));
//                System.out.println("dailyrate: " + rs.getInt(9));
//            }
            return rs;
        } catch(final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * T2. find most expensive posts
     * @return ResultSet rs
     */
    public static ResultSet findMostExpensivePosts() {
        try {
            final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
            final Connection con = persistenceLayer.getConnection();
            final Statement stmt = con.createStatement();
            // stmt is a statement object
            final ResultSet rs = stmt.executeQuery("SELECT * FROM PostingInfo PI WHERE PI.dailyrate = (SELECT MAX(dailyrate) FROM PostingInfo)");
            // pid, fromdate, todate, hostid, hostname, roomno, residencename, university, dailyrate
//            while (rs.next()) {
//                System.out.println("pid: " + rs.getInt(1));
//                System.out.println("fromdate: " + rs.getString(2));
//                System.out.println("todate: " + rs.getString(3));
//                System.out.println("hostid: " + rs.getInt(4));
//                System.out.println("hostname: " + rs.getString(5));
//                System.out.println("roomno: " + rs.getString(1));
//                System.out.println("residencename: " + rs.getString(2));
//                System.out.println("university: " + rs.getString(3));
//                System.out.println("dailyrate: " + rs.getInt(4));
//            }
            return rs;
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

    private static int getMaxPostingId() throws SQLException {
        final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
        final Connection con = persistenceLayer.getConnection();
        final PreparedStatement ps1 = con.prepareStatement("SELECT MAX(pid) FROM Posting");
        final ResultSet rs = ps1.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
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
