package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BookingModel;

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

    public ArrayList<BookingModel> getBookingInBranchString(int branch_id) {
        ArrayList<BookingModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM BOOKING " +
                    "WHERE BRANCH_ID = ?");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingModel bookingModel = new BookingModel(rs.getInt("booking_id"),
                        rs.getDate("booking_date"),
                        rs.getTime("booking_date"),
                        rs.getInt("member_id"),
                        rs.getInt("branch_id"));

                result.add(bookingModel);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public String getBookablesByBookingString(int bookingId) {
        String result = "";

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM RESERVE, BOOKABLE " +
                        "WHERE RESERVE.BOOKING_ID = ? AND " +
                        "      RESERVE.BOOKABLE_ID = BOOKABLE.BOOKABLE_ID"
            );

            ps.setInt(1, bookingId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result = result +
                        rs.getString("bookable_name") + " " +
                        rs.getString("bookable_id") + ", ";
            }

            if (!result.equals("")) {
                result = result.substring(0, result.length() - 2);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "3 " + e.getMessage());
        }

        return result;
    }

    public ArrayList<String> getBookingByMemberId(int mid) {
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM MEMBER m, BOOKING b, BRANCH br " +
                    "WHERE m.MEMBER_ID = b.MEMBER_ID " +
                    "AND br.BRANCH_ID = b.BRANCH_ID " +
                    "AND b.MEMBER_ID = ?");

            ps.setInt(1, mid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(
                        rs.getString("branch_name") + "    |" +
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
