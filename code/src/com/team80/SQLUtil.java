package com.team80;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class SQLUtil {
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String DELETE_FROM = "DELETE FROM ";
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String SELECT_COUNT = "SELECT COUNT(*) ";

    private static Connection getConnection() {
        final PersistenceLayer persistenceLayer = PersistenceLayer.getInstance();
        return persistenceLayer.getConnection();
    }

    private static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * H1. a host can make a new post
     */
    public static int addPost(final int hostid, final Date fromDate, final Date toDate, final String description) {
        try {
            final String sql = INSERT_INTO + "Posting VALUES (?, ?, ?, ?, ?)";
            final PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, getMaxPostingId() + 1);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ps.setString(4, description);
            ps.setInt(5, hostid);
            return ps.executeUpdate();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * H2. a host can see all of his or her own postings
     */
    public static ResultSet findHostsPostings(final int hostid) {
        try {
            return getStatement().executeQuery(
                    "SELECT PI.pid, PI.fromdate, PI.todate, PI.roomno, PI.residencename, PI.dailyrate, PI.description " +
                    "FROM PostingInfo PI " +
                    "WHERE PI.hostid = " + hostid);
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * H3. a host can update his post
     */
    public static int updatePost(final int[] selection, final DefaultTableModel model, final Date fromDate,
                                 final Date toDate, final String description) {
        final Date fDate = (fromDate == null) ? (Date) model.getValueAt(selection[0], 1) : fromDate;
        final Date tDate = (toDate == null) ? (Date) model.getValueAt(selection[0], 2) : toDate;
        if (fDate.after(tDate)) {
            return 0;
        } else {
            final int pid = (Integer) model.getValueAt(selection[0], 0);
            return updatePostSQL(pid, fDate, toDate, description);
        }
    }

    private static int updatePostSQL(final int pid, final Date fromDate, final Date toDate, final String description) {
        try {
            final Connection con = getConnection();
            final PreparedStatement ps = con.prepareStatement(
                    "UPDATE Posting SET description = ? , fromdate = ? , todate = ? WHERE pid = ?");
            ps.setString(1, description);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ps.setInt(4, pid);
            return ps.executeUpdate();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * H4. a host can see all his contracts
     */
    public static ResultSet findHostsContracts(final int hostid) {
        try {
            return getStatement().executeQuery(
                    "SELECT CS.contractid, CS.fromdate, CS.todate, CS.travelerid, S.name, U.name " +
                    "FROM Contract_Signs CS, Traveler T, Student S, University U " +
                    "WHERE CS.hostid = " + hostid + " AND T.cid = CS.travelerid AND T.cid = S.cid AND S.unid = U.unid");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * H5. a host can see all the ratings he or she has reviewed
     */
    public static ResultSet findHostsReviews(final int hostid) {
        try {
            return getStatement().executeQuery("SELECT HR.travelerid, S.name ,HR.rating " +
                    "FROM Host_Reviews HR, Traveler T, Student S " +
                    "WHERE HR.hostid = " + hostid + "AND HR.travelerid = T.cid AND T.cid = S.cid");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * H6. a host can review a traveler
     */
    public static int addHostReview(final int hostid, final int travelerid, final int rating) {
        try {
            if (hostReviewExists(hostid, travelerid)) {
                System.out.println("A review already exists, update the review");
                return updateHostReview(hostid, travelerid, rating);
            } else {
                System.out.println("A Host is adding a new review to a traveler");
                return addNewHostReview(hostid, travelerid, rating);
            }
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    private static int addNewHostReview(final int hostid, final int travelerid, final int rating) throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement(INSERT_INTO + "Host_Reviews VALUES (?, ?, ?)");
        ps.setInt(1, travelerid);
        ps.setInt(2, hostid);
        ps.setInt(3, rating);
        System.out.println("Host has added a new review to a traveler");
        return ps.executeUpdate();
    }

    private static int updateHostReview(final int hostid, final int travelerid, final int rating) throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE Host_Reviews SET rating = ? WHERE travelerid = ? AND hostid = ?");
        ps.setInt(1, rating);
        ps.setInt(2, travelerid);
        ps.setInt(3, hostid);
        return ps.executeUpdate();
    }

    private static boolean hostReviewExists(final int hostid, final int travelerid) throws SQLException {
        final ResultSet rs = getStatement().executeQuery(SELECT_COUNT + "FROM Host_Reviews " +
                "WHERE hostid = " + hostid + " AND travelerid = " + travelerid);
        return rs.next() && rs.getInt(1) > 0;
    }

    /**
     * H7. a host can delete his post
     */
    public static int[] deletePost(int[] selection, DefaultTableModel model) {
        try {
            final PreparedStatement ps = getConnection().prepareStatement(DELETE_FROM + "Posting WHERE pid = ?");
            for (final int column : selection) {
                final int pid = (Integer) model.getValueAt(column, 0);
                ps.setInt(1, pid);
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * T1. find postings with
     */
    public static ResultSet findAllPosts(final int id) {
        try {
            final String stmt = SELECT_ALL_FROM + ((id == -1) ?
                    "Posting" : "Posting WHERE hostid = ? ORDER BY pid DESC");
            final PreparedStatement ps = getConnection().prepareStatement(stmt);
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * T2. find cheapest/most expensive posts
     */
    public static ResultSet findCheapestPosts() {
        try {
            return getStatement().executeQuery(
                    SELECT_ALL_FROM + "PostingInfo PI WHERE PI.dailyrate = (SELECT MIN(dailyrate) FROM PostingInfo)");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet findMostExpensivePosts() {
        try {
            return getStatement().executeQuery(
                    SELECT_ALL_FROM + "PostingInfo PI WHERE PI.dailyrate = (SELECT MAX(dailyrate) FROM PostingInfo)");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * T3. a traveler can sign up contract
     */
    public static int signContract(final int hostid, final int travelerid, final Date fromDate, final Date toDate) {
        try {
            final int contractid = getMaxContractId() + 1;
            final PreparedStatement ps = getConnection().prepareStatement(
                    INSERT_INTO + "Contract_Signs VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, contractid);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ps.setInt(4, hostid);
            ps.setInt(5, travelerid);
            System.out.println("A traveler has signed up a contract");
            return ps.executeUpdate();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * T4. a traveler can view his or her contracts
     */
    public static ResultSet findTravelerContracts(final int travlerid) {
        try {
            return getStatement().executeQuery(
                    "SELECT CS.contractid, CS.fromdate, CS.todate, HI.hostid, HI.hostname, HI.university, HI.roomno, HI.residencename " +
                    "FROM Contract_Signs CS, HostInfo HI " +
                    "WHERE CS.travelerid = " + travlerid + " AND CS.hostid = HI.hostid");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    private static int getMaxContractId() throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement("SELECT MAX(contractid) FROM Contract_Signs");
        final ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    private static int getMaxPostingId() throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement("SELECT MAX(pid) FROM Posting");
        final ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public static ResultSet getHost(final int hostid) {
        try {
            final PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_FROM + "Hosts WHERE cid = ?");
            ps.setInt(1, hostid);
            return ps.executeQuery();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static int addHost(final int cid, final String roomNo, final String residence, final int dailyRate) {
        try {
            final PreparedStatement ps = getConnection().prepareStatement(INSERT_INTO + "Hosts VALUES (?, ?, ?, ?)");
            ps.setInt(1, cid);
            ps.setString(2, roomNo);
            ps.setString(3, residence);
            ps.setInt(4, dailyRate);
            return ps.executeUpdate();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public static ResultSet getStudent(final int cid) {
        try {
            final PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_FROM + "Student WHERE cid = ?");
            ps.setInt(1, cid);
            return ps.executeQuery();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

}
