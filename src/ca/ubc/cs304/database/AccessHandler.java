package ca.ubc.cs304.database;

import ca.ubc.cs304.model.AccessRelation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
