package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BookingModel;
import ca.ubc.cs304.model.MemberModel;

import java.sql.*;
import java.util.ArrayList;

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

    public ArrayList<String> getBookingInBranchString(int branch_id) {
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT FIRST_NAME, LAST_NAME, BOOKING_DATE " +
                    "FROM MEMBER m, BOOKING b " +
                    "WHERE b.MEMBER_ID = m.MEMBER_ID " +
                    "  AND b.BRANCH_ID = ?");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("first_name") + " " +
                            rs.getString("last_name") + "     | " +
                            rs.getTime("booking_date").toString());
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void insertBooking(BookingModel bookingModel) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO BOOKING VALUES (?,?,?,?)");
            ps.setInt(1, bookingModel.getId());
            ps.setDate(2, bookingModel.getDate());
            ps.setInt(3, bookingModel.getMemberId());
            ps.setInt(4, bookingModel.getBranchId());

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
