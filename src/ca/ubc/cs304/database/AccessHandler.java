package ca.ubc.cs304.database;

import ca.ubc.cs304.model.AccessRelation;

import java.sql.*;
import java.util.ArrayList;

public class AccessHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public AccessHandler(Connection connection) {
        this.connection = connection;
    }

    // AGGREGATION with GROUP BY, HAVING
    // Find users that have accessed public areas before and their # of accesses
    public void avgAccessGroupedByDateHaving() {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT MEMBER_ID, COUNT(MEMBER_ID)" +
                            "FROM \"ACCESS\"" +
                            "GROUP BY MEMBER_ID" +
                            "HAVING COUNT(MEMBER_ID) >= 1");
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ArrayList<String> getAccessInBranchString(int branch_id) {
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT m.FIRST_NAME, p.AREA_NAME " +
                            "FROM ACCESS a, MEMBER m, PUBLIC_AREA p " +
                            "WHERE a.MEMBER_ID = m.MEMBER_ID " +
                            "  AND p.AREA_ID = a.AREA_ID " +
                            "  AND m.MEMBER_ID in (" +
                            "      SELECT MEMBER_ID " +
                            "      FROM SIGN_UP " +
                            "      WHERE BRANCH_ID = ? " +
                            ")");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("first_name") + " " +
                        rs.getString("area_name"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
