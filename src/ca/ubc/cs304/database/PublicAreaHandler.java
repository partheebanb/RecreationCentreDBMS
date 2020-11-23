package ca.ubc.cs304.database;

import ca.ubc.cs304.model.AccessRelation;
import ca.ubc.cs304.model.PublicAreaModel;

import java.sql.*;
import java.util.ArrayList;

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

    public ArrayList<PublicAreaModel> getAreaInfo() {
        ArrayList<PublicAreaModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public_area");

            while (rs.next()) {
                PublicAreaModel model = new PublicAreaModel(
                        rs.getInt("areaId"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBoolean("isOutdoor"),
                        rs.getInt("branchId"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
