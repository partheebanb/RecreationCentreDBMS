package ca.ubc.cs304.database;

import ca.ubc.cs304.model.DayEventModel;
import ca.ubc.cs304.model.MemberModel;
import ca.ubc.cs304.model.ReserveRelation;

import java.sql.*;
import java.util.ArrayList;

public class EventHandler {
    private static final String EXCEPTION_TAG = "[EVENT HANDLER EXCEPTION]";
    private static final String WARNING_TAG = "[EVENT HANDLER WARNING]";

    Connection connection;

    public EventHandler(Connection connection) {
        this.connection = connection;
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

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + "1 " + e.getMessage());
        }
    }
}
