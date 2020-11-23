package ca.ubc.cs304.database;

import ca.ubc.cs304.model.PublicAreaModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PublicAreaHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public PublicAreaHandler(Connection connection) {
        this.connection = connection;
    }

    public void deletePublicArea(int areaId) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM public_area WHERE area_id = ?");
            ps.setInt(1, areaId);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " PublicArea " + areaId + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertPublicArea(PublicAreaModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO public_area VALUES (?,?,?,?,?)");
            ps.setInt(1, model.getAreaId());
            ps.setString(2, model.getType());
            ps.setString(3, model.getName());
            ps.setBoolean(4, model.isOutdoor());
            ps.setInt(5, model.getBranchId());


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
