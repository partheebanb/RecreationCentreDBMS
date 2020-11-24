package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BranchModel;

import java.sql.*;
import java.util.ArrayList;

public class BranchHandler {
    private static final String EXCEPTION_TAG = "[BRANCH HANDLER EXCEPTION]";
    private static final String WARNING_TAG = "[BRANCH HANDLER WARNING]";

    Connection connection;

    public BranchHandler(Connection connection) {
        this.connection = connection;
    }

    // Get all the BranchModel
    public ArrayList<BranchModel> getBranchInfo() {
        ArrayList<BranchModel> result = new ArrayList<BranchModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM branch");

            while(rs.next()) {
                BranchModel model = new BranchModel(rs.getInt("branch_id"),
                        rs.getString("branch_name"),
                        rs.getString("branch_address"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    // Get BranchModel given a specific branch_id
    public BranchModel getBranchByBranchId(int branchId) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM BRANCH " +
                        "WHERE BRANCH_ID = ?");

            ps.setInt(1, branchId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                BranchModel model = new BranchModel(rs.getInt("branch_id"),
                        rs.getString("branch_name"),
                        rs.getString("branch_address"));
                return model;
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return null;
    }

    // Get all the names of the branches that a member signs up in
    public ArrayList<String> getBranchNameByMemberId(int memberId) {
        ArrayList<String> result = new ArrayList<String>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT BRANCH_NAME " +
                    "FROM branch b, sign_up s " +
                    "WHERE s.MEMBER_ID = ? AND " +
                            "b.BRANCH_ID = s.BRANCH_ID");

            ps.setInt(1, memberId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                result.add(rs.getString("branch_name"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }
}
