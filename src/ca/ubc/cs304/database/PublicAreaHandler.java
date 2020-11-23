package ca.ubc.cs304.database;

import ca.ubc.cs304.model.AccessRelation;
import ca.ubc.cs304.model.BookableModel;
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

    public ArrayList<PublicAreaModel> getPublicAreaInfoInBranch(int branch_id) {
        ArrayList<PublicAreaModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM PUBLIC_AREA " +
                    "WHERE BRANCH_ID = ?");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                PublicAreaModel model = new PublicAreaModel(rs.getInt("area_id"),
                        rs.getString("area_name"),
                        rs.getBoolean("area_is_outdoor"),
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
            ps.setString(2, model.getName());
            ps.setBoolean(3, model.isOutdoor());
            ps.setInt(4, model.getBranchId());


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
