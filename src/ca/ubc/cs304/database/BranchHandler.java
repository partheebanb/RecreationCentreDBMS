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

    public void deleteBranch(int branchId) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
            ps.setInt(1, branchId);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertBranch(BranchModel model) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?)");
            ps.setInt(1, model.getId());
            ps.setString(2, model.getName());
            ps.setString(3, model.getAddress());


            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

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

    public BranchModel getBranchInfoByBranchId(int branchId) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM BRANCH " +
                        "WHERE BRANCH_ID = ?");

            ps.setInt(1, branchId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
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

    public ArrayList<String> getBranchNamesByMemberId(int memberId) {
        ArrayList<String> result = new ArrayList<String>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
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

    public void updateBranch(int id, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
            ps.setString(1, name);
            ps.setInt(2, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
            }

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
