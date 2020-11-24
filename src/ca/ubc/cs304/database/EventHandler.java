package ca.ubc.cs304.database;

import ca.ubc.cs304.model.*;

import java.sql.*;
import java.util.ArrayList;

public class EventHandler {
    private static final String EXCEPTION_TAG = "[EVENT HANDLER EXCEPTION]";
    private static final String WARNING_TAG = "[EVENT HANDLER WARNING]";

    Connection connection;

    public EventHandler(Connection connection) {
        this.connection = connection;
    }

    public int getNextId() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MAX(EVENT_ID) " +
                    "FROM EVENT");

            ResultSet maxBookingId = ps.executeQuery();
            if (maxBookingId.next()) {
                return maxBookingId.getInt("MAX(EVENT_ID)") + 1;
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return -1;
    }

    public void insertEvent(DayEventModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO EVENT (EVENT_ID, NAME, EVENT_DATETIME, BRANCH_ID) " +
                    "VALUES (?,?,?,?)");
            ps.setInt(1, model.getEventId());
            ps.setString(2, model.getName());
            ps.setDate(3, model.getDate());
            ps.setInt(4, model.getBranch_id());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertUse(UseRelation useRelation) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO USE (EVENT_ID, BOOKABLE_ID) " +
                            "VALUES (?,?)");
            ps.setInt(1, useRelation.getEventId());
            ps.setInt(2, useRelation.getBookableId());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertAttend(AttendRelation attendRelation) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ATTEND (EVENT_ID, MEMBER_ID) " +
                            "VALUES (?,?)");
            ps.setInt(1, attendRelation.getEventId());
            ps.setInt(2, attendRelation.getMemberId());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ArrayList<DayEventModel> getEventModelOnBranch(int branchId) {
        ArrayList<DayEventModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                    "FROM EVENT " +
                    "WHERE BRANCH_ID = ?");

            ps.setInt(1, branchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DayEventModel model = new DayEventModel(
                        rs.getInt("event_id"),
                        rs.getString("name"),
                        rs.getDate("event_datetime"),
                        rs.getTime("event_datetime"),
                        rs.getInt("branch_id"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "4 " + e.getMessage());
        }

        return result;
    }

    public ArrayList<DayEventModel> getEventModelForMember(int memberId) {
        ArrayList<DayEventModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM EVENT, ATTEND " +
                        "WHERE EVENT.EVENT_ID = ATTEND.EVENT_ID " +
                        "    AND MEMBER_ID = ? ");

            ps.setInt(1, memberId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DayEventModel model = new DayEventModel(
                        rs.getInt("event_id"),
                        rs.getString("name"),
                        rs.getDate("event_datetime"),
                        rs.getTime("event_datetime"),
                        rs.getInt("branch_id"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "4 " + e.getMessage());
        }

        return result;
    }

    public String getEmployeeManagingEventString(int eventId) {
        String result = "";

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM HOSTS, EMPLOYEE " +
                        "WHERE HOSTS.EVENT_ID = ? AND " +
                        "      EMPLOYEE.EMPLOYEE_ID = HOSTS.EMPLOYEE_ID"
            );

            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result = result +
                        rs.getString("employee_first_name") + " " +
                        rs.getString("employee_last_name") + ", ";
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

    public String getMemberAttendingEvent(int eventId) {
        String result = "";

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM ATTEND, MEMBER " +
                        "WHERE ATTEND.EVENT_ID = ? AND " +
                        "      MEMBER.MEMBER_ID = ATTEND.MEMBER_ID"
            );

            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result = result +
                        rs.getString("first_name") + " " +
                        rs.getString("last_name") + ", ";
            }

            if (!result.equals("")) {
                result = result.substring(0, result.length() - 2);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "2 " + e.getMessage());
        }

        return result;
    }

    public String getEquipmentsUsedInEventString(int eventId) {
        String result = "";

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM USE, BOOKABLE " +
                        "WHERE USE.EVENT_ID = ? AND " +
                        "      USE.BOOKABLE_ID = BOOKABLE.BOOKABLE_ID AND " +
                        "      USE.BOOKABLE_ID in " +
                        "          (SELECT BOOKABLE_ID " +
                        "           FROM EQUIPMENT)"
            );

            ps.setInt(1, eventId);

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
            System.out.println(EXCEPTION_TAG + "2 " + e.getMessage());
        }

        return result;
    }

    public String getRoomsUsedInEventString(int eventId) {
        String result = "";

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM USE, BOOKABLE " +
                        "WHERE USE.EVENT_ID = ? AND " +
                        "      USE.BOOKABLE_ID = BOOKABLE.BOOKABLE_ID AND " +
                        "      USE.BOOKABLE_ID in " +
                        "          (SELECT BOOKABLE_ID " +
                        "           FROM ROOM)"
            );

            ps.setInt(1, eventId);

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
            System.out.println(EXCEPTION_TAG + "2 " + e.getMessage());
        }

        return result;
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "1 " + e.getMessage());
        }
    }
}
