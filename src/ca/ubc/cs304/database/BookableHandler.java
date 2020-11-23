package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BookableModel;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.EquipmentModel;

import java.sql.*;
import java.util.ArrayList;

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
    public void avgBookableGroupedByEvents() {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(BOOKABLE.BOOKABLE_ID), EVENT.EVENT_ID " +
                            "FROM BOOKABLE, EVENT, USE " +
                            "WHERE BOOKABLE.BOOKABLE_ID = USE.BOOKABLE_ID AND USE.EVENT_ID = EVENT.EVENT_ID " +
                            "GROUP BY (EVENT.EVENT_ID)");
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // nested aggregation
    // count the number of bookables grouped by events
    // return only the result with the earliest event date
    public void countOfBookablesUsedInTheLatestEventDate() {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(BOOKABLE.BOOKABLE_ID), EVENT.EVENT_DATETIME" +
                            "FROM BOOKABLE, EVENT, USE" +
                            "WHERE BOOKABLE.BOOKABLE_ID = USE.BOOKABLE_ID AND USE.EVENT_ID = EVENT.EVENT_ID" +
                            "GROUP BY (EVENT.EVENT_DATETIME)" +
                            "HAVING (EVENT_DATETIME) = (SELECT MAX(EVENT_DATETIME)" +
                            "                            FROM EVENT)");
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