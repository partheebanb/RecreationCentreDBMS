package ca.ubc.cs304.database;

import ca.ubc.cs304.model.EquipmentModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookableHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public BookableHandler(Connection connection) {
        this.connection = connection;
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
                    "SELECT COUNT(BOOKABLE.BOOKABLE_ID), EVENT.EVENT_ID\n" +
                            "FROM BOOKABLE, EVENT, USE\n" +
                            "WHERE BOOKABLE.BOOKABLE_ID = USE.BOOKABLE_ID AND USE.EVENT_ID = EVENT.EVENT_ID\n" +
                            "GROUP BY (EVENT.EVENT_ID)");
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