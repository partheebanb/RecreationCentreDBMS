package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BookableModel;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.EquipmentModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BookableHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection;

    public BookableHandler(Connection connection) {
        this.connection = connection;
    }

    // Get the BookableModel of all the equipments in a branch
    public ArrayList<BookableModel> getEquipmentInfoInBranch(int branch_id) {
        ArrayList<BookableModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM BOOKABLE " +
                    "WHERE BOOKABLE_ID IN ( " +
                    "    SELECT BOOKABLE_ID " +
                    "    FROM EQUIPMENT " +
                    "    ) AND BRANCH_ID = ?");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                BookableModel model = new BookableModel(rs.getInt("bookable_id"),
                        rs.getString("bookable_type"),
                        rs.getString("bookable_name"),
                        rs.getInt("branch_id"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    // Get the BookableModel of all the rooms in a branch
    public ArrayList<BookableModel> getRoomInfoInBranch(int branch_id) {
        ArrayList<BookableModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM BOOKABLE " +
                    "WHERE BOOKABLE_ID IN ( " +
                    "    SELECT BOOKABLE_ID " +
                    "    FROM ROOM " +
                    "    ) AND BRANCH_ID = ?");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                BookableModel model = new BookableModel(rs.getInt("bookable_id"),
                        rs.getString("bookable_type"),
                        rs.getString("bookable_type"),
                        rs.getInt("branch_id"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    // AGGREGATION with GROUP BY
    // Find the number of bookable every event books
    public HashMap<String, Integer> bookableCountGroupedByEvents() {
        HashMap<String, Integer> eventToUses = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT NAME, G.EVENT_ID, \"COUNT(BOOKABLE.BOOKABLE_ID)\" " +
                    "FROM EVENT, (SELECT EVENT.EVENT_ID, COUNT(BOOKABLE.BOOKABLE_ID) " +
                    "    FROM BOOKABLE, EVENT, USE " +
                    "    WHERE BOOKABLE.BOOKABLE_ID = USE.BOOKABLE_ID AND USE.EVENT_ID = EVENT.EVENT_ID " +
                    "    GROUP BY (EVENT.EVENT_ID)) G " +
                    "    WHERE G.EVENT_ID = EVENT.EVENT_ID");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                eventToUses.put(rs.getString("name") + " " + rs.getString("event_id"), rs.getInt("count(bookable.bookable_id)"));
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return eventToUses;
    }

    // NESTED AGGREGATION
    // count the number of bookable grouped by events
    // return only the result with the earliest event date
    public int countOfBookablesUsedInTheLatestEventDate() {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(BOOKABLE.BOOKABLE_ID) " +
                            "FROM BOOKABLE, EVENT, USE " +
                            "WHERE BOOKABLE.BOOKABLE_ID = USE.BOOKABLE_ID AND USE.EVENT_ID = EVENT.EVENT_ID " +
                            "GROUP BY (EVENT.EVENT_DATETIME)" +
                            "HAVING (EVENT_DATETIME) = (SELECT MAX(EVENT_DATETIME) " +
                            "                            FROM EVENT)");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return rs.getInt("count(bookable.bookable_id)");
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return -1;
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}