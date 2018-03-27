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

    public static boolean hostExists(final int hostid) {
        try {
            final ResultSet rs = getStatement().executeQuery(
                    "SELECT H.cid FROM Student S, Hosts H " +
                            "WHERE H.cid = S.cid AND H.cid = " + hostid);
            return rs.next();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static boolean travelerExists(final int travelerid) {
        try {
            final ResultSet rs = getStatement().executeQuery(
                    "SELECT T.cid FROM Student S, Traveler T " +
                            "WHERE T.cid = S.cid AND T.cid = " + travelerid);
            return rs.next();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static boolean hasCorrectPassword(final int cid, final String password) {
        try {
            final ResultSet rs = getStatement().executeQuery(
                    "SELECT S.cid FROM Student S " +
                            "WHERE S.cid = " + cid + " AND S.password = " + password);
            return rs.next();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return false;
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

    private static int getMaxPostingId() throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement("SELECT MAX(pid) FROM Posting");
        final ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
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
            return updatePostSQL(pid, fDate, tDate, description);
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
                    "SELECT CS.contractid, CS.fromdate, CS.todate, CS.travelerid, S.name, U.name AS UNIVERSITY " +
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
     * H7. a host can delete his post(s)
     */
    public static int[] deletePost(final int[] selection, final DefaultTableModel model) {
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
     * T1. a traveler can find postings with
     --     I. conditions:
     --        a) university
     --        b) within date range (fromdate <= STARTDATE AND todate >= ENDDATE)
     --        c) daily rate range (daily rate BETWEEN AMT1 AND AMT2) default: low: 1 high: 200
     --    II. display options: has to display pid at least
     --        a) date range (fromdate + todate)
     --        b) host name
     --        c) address (roomno + residencename)
     --        d) university
     --        e) dailyrate
     */
    public static ResultSet findPostsWith(final boolean showDateRange, final boolean showHost,
                                          final boolean showAddress, final boolean showUniv,
                                          final boolean showRate) {
        try {
            final String select = computeSelectPosts(showDateRange, showHost, showAddress, showUniv, showRate);
            final String sql = select + "FROM PostingInfo PI ";
            final PreparedStatement ps = getConnection().prepareStatement(sql);
            return ps.executeQuery();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet findPostsWithCondition(final String univ, final Date startDate, final Date endDate,
                                                   final int lowPrice, final int highPrice,
                                                   final boolean showDateRange, final boolean showHost,
                                                   final boolean showAddress, final boolean showUniv,
                                                   final boolean showRate) {
        try {
            final String select = computeSelectPosts(showDateRange, showHost, showAddress, showUniv, showRate);
            final String sql = select + "FROM PostingInfo PI " + computeWherePosts(univ, startDate, endDate, lowPrice, highPrice);
            final PreparedStatement ps = getConnection().prepareStatement(sql);
            return ps.executeQuery();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    private static String computeSelectPosts(final boolean showDateRange,
                                             final boolean showHost, final boolean showAddress,
                                             final boolean showUniv, final boolean showRate) {
        String select = "SELECT PI.pid,";
        if (showDateRange) {
            select += "PI.fromdate,PI.todate,";
        }
        if (showHost) {
            select += "PI.hostname,";
        }
        if (showAddress) {
            select += "PI.roomno,PI.residencename,";
        }
        if (showUniv) {
            select += "PI.university,";
        }
        if (showRate) {
            select += "PI.dailyrate,";
        }
        return select.substring(0, select.length()-1) + " ";
    }

    private static String computeWherePosts(final String university, final Date startDate, final Date endDate, final int
            lowPrice, final int highPrice) {
        String where = "WHERE PI.dailyrate BETWEEN " + lowPrice + " AND " + highPrice;
        if (university != null && !university.isEmpty()) {
            where += " AND PI.university LIKE '%" + university + "%' ";
        }
        if (startDate != null) {
            where += " AND PI.fromdate <= " + startDate;
        }
        if (endDate != null) {
            where += " AND PI.todate >= " + endDate;
        }
        return where;
    }

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
            final PreparedStatement ps = getConnection().prepareStatement(
                    INSERT_INTO + "Contract_Signs VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, getMaxContractId() + 1);
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

    private static int getMaxContractId() throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement("SELECT MAX(contractid) FROM Contract_Signs");
        final ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
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

    /**
     * T5. a traveler can cancel his or her contract if the contract in the future
     */
    public static int deleteContract(final int contractid) {
        try {
            if (isFutureContract(contractid)) {
                final PreparedStatement ps = getConnection().prepareStatement(
                        DELETE_FROM + "Contract_Signs WHERE contractid = ?");
                ps.setInt(1, contractid);
                return ps.executeUpdate();
            } else {
                System.out.println("The contract is current or past, you cannot delete it!");
                return 0;
            }
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    private static boolean isFutureContract(final int contractid) throws SQLException {
        final ResultSet rs = getStatement().executeQuery(
                "SELECT CS.fromdate FROM Contract_Signs CS WHERE CS.contractid = " + contractid);
        if (rs.next()) {
            final Date fromDate = rs.getDate(1);
            final Date today = new Date(System.currentTimeMillis());
            return today.before(fromDate);
        } else {
            return false;
        }
    }

    /**
     * T6. a traveler can review a host
     */
    public static int addTravelerReview(final int travelerid, final int hostid, final int rating) {
        try {
            if (travelerReviewExists(travelerid, hostid)) {
                System.out.println("A review already exists, update the review");
                return updateTravelerReview(travelerid, hostid, rating);
            } else {
                System.out.println("A traveler is adding a new review to a host");
                return addNewTravelerReview(travelerid, hostid, rating);
            }
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    private static int addNewTravelerReview(final int hostid, final int travelerid, final int rating)
            throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement(
                INSERT_INTO + "Traveler_Reviews VALUES (?, ?, ?)");
        ps.setInt(1, travelerid);
        ps.setInt(2, hostid);
        ps.setInt(3, rating);
        return ps.executeUpdate();
    }

    private static int updateTravelerReview(final int travelerid, final int hostid, final int rating)
            throws SQLException {
        final PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE Traveler_Reviews SET rating = ? WHERE travelerid = ? AND hostid = ?");
        ps.setInt(1, rating);
        ps.setInt(2, travelerid);
        ps.setInt(3, hostid);
        return ps.executeUpdate();
    }

    private static boolean travelerReviewExists(final int travelerid, final int hostid) throws SQLException {
        final ResultSet rs = getStatement().executeQuery(SELECT_COUNT + "FROM Traveler_Reviews " +
                "WHERE hostid = " + hostid + " AND travelerid = " + travelerid);
        return rs.next() && rs.getInt(1) > 0;
    }

    /**
     * A1. an administrator can find host info and their contract counts
     */
    public static ResultSet findHostsContracts() {
        try {
            return getStatement().executeQuery(
                    "SELECT HI.hostid, HI.hostname, HI.university, COUNT(CS.contractid) " +
                    "FROM HostInfo HI, Contract_Signs CS " +
                    "WHERE HI.hostid = CS.hostid " +
                    "GROUP BY HI.hostid " +
                    "HAVING COUNT(CS.contractid) > 0");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * A2. an administrator can find highest/lowest rated hosts
     */
    public static ResultSet findBestHosts() {
        try {
            return getStatement().executeQuery("WITH HostRating(hostid, rating) AS " +
                    "     (SELECT TR.hostid, AVG(TR.rating) FROM Traveler_Reviews TR GROUP BY TR.hostid) " +
                    "SELECT HI.hostid, HI.hostname, HI.university, HR.rating " +
                    "FROM HostInfo HI, HostRating HR " +
                    "WHERE HI.hostid = HR.hostid AND HR.rating = (SELECT MAX(rating) FROM HostRating)");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet findWorstHosts() {
        try {
            return getStatement().executeQuery("WITH HostRating(hostid, rating) AS " +
                    "     (SELECT TR.hostid, AVG(TR.rating) FROM Traveler_Reviews TR GROUP BY TR.hostid) " +
                    "SELECT HI.hostid, HI.hostname, HI.university, HR.rating " +
                    "FROM HostInfo HI, HostRating HR " +
                    "WHERE HI.hostid = HR.hostid AND HR.rating = (SELECT MIN(rating) FROM HostRating)");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * A3. an administrator can delete a host
     */
    public static int deleteHost(final int hostid) {
        try {
            final PreparedStatement ps = getConnection().prepareStatement(
                    DELETE_FROM + "Hosts WHERE cid = ?");
            ps.setInt(1, hostid);
            return ps.executeUpdate();
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * A4. an administrator can find travelers who have been to every university
     */
    public static ResultSet findAmazingTravelers() {
        try {
            return getStatement().executeQuery("SELECT S.cid, S.name FROM Traveler T, Student S " +
                    "WHERE T.cid = S.cid AND NOT EXISTS " +
                    "    ((SELECT U1.unid FROM University U1 " +
                    "      WHERE U1.unid NOT IN (SELECT U2.unid FROM University U2 WHERE S.unid = U2.unid))" +
                    "      MINUS " +
                    "     (SELECT U3.unid FROM Contract_Signs CS, Hosts H, Student S2, University U3 " +
                    "      WHERE CS.travelerid = T.cid AND CS.hostid = H.cid AND H.cid = S2.cid AND S2.unid = U3.unid))");
        } catch (final SQLException e) {
            System.err.println("An error occurred while executing query.");
            System.err.println(e.getMessage());
        }
        return null;
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
