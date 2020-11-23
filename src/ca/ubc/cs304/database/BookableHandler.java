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

    private Connection connection = null;

    public BookableHandler(Connection connection) {
        this.connection = connection;
    }

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

    public void insertEquipment(EquipmentModel model) {
        insertToBookable(model);
        insertToEquipment(model);
    }

    private void insertToBookable(EquipmentModel model) {
        // for bookable table
        PreparedStatement psb = null;
        try {
            psb = connection.prepareStatement("INSERT INTO bookable VALUES (?,?,?,?)");
            psb.setInt(1, model.getBookableId());
            psb.setString(2, model.getType());
            psb.setString(3, model.getName());
            psb.setInt(4, model.getBranchId());
            psb.executeUpdate();
            connection.commit();
            psb.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void insertToEquipment(EquipmentModel model) {
        // for equipment table
        try {
            PreparedStatement pse = connection.prepareStatement("INSERT INTO equipment VALUES (?,?,?)");
            pse.setInt(1, model.getBookableId());
            pse.setDate(2, model.getPurchased());
            pse.setDate(3, model.getLastFixed());
            pse.executeUpdate();
            connection.commit();
            pse.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // AGGREGATION with GROUP BY
    public HashMap<String, Integer> avgBookableGroupedByEvents() {
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

    // nested aggregation
    // count the number of bookables grouped by events
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