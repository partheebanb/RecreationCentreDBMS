package ca.ubc.cs304.database;

import ca.ubc.cs304.model.ReserveRelation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReserveHandler {
    private static final String EXCEPTION_TAG = "[RESERVE HANDLER EXCEPTION]";
    private static final String WARNING_TAG = "[RESERVE HANDLER WARNING]";

    Connection connection;

    public ReserveHandler(Connection connection) {
        this.connection = connection;
    }

    public void insertReserve(ReserveRelation reserveRelation) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO RESERVE VALUES (?,?)");
            ps.setInt(1, reserveRelation.getBookingId());
            ps.setInt(2, reserveRelation.getBookableId());

            ps.executeUpdate();
            connection.commit();

            ps.close();
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
