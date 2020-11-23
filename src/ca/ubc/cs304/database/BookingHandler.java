package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BookingModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingHandler {
    private static final String EXCEPTION_TAG = "[BOOKING HANDLER EXCEPTION]";
    private static final String WARNING_TAG = "[BOOKING HANDLER WARNING]";

    Connection connection;

    public BookingHandler(Connection connection) {
        this.connection = connection;
    }

    public int getNextId() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MAX(BOOKING_ID) " +
                    "FROM BOOKING");

            ResultSet maxBookingId = ps.executeQuery();
            if (maxBookingId.next()) {
                return maxBookingId.getInt("MAX(BOOKING_ID)") + 1;
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return -1;
    }

    public void insertBooking(BookingModel bookingModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO BOOKING VALUES (?,?,?)");
            ps.setInt(1, bookingModel.getId());
            ps.setDate(2, bookingModel.getDate());
            ps.setInt(3, bookingModel.getMemberId());


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
