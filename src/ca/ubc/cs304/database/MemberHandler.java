package ca.ubc.cs304.database;

import ca.ubc.cs304.model.MemberModel;

import java.sql.*;
import java.util.ArrayList;

public class MemberHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public MemberHandler(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<MemberModel> getMemberInfo() {
        ArrayList<MemberModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM member");

            while (rs.next()) {
                MemberModel model = new MemberModel(rs.getInt("member_id"),
                        rs.getDate("member_since"),
                        rs.getDate("dob"),
                        rs.getString("membership_type"),
                        rs.getString("gender").charAt(0),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public ArrayList<MemberModel> getMemberInfoInBranch(int branch_id) {
        ArrayList<MemberModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM MEMBER " +
                    "WHERE MEMBER_ID IN (" +
                    "    SELECT MEMBER_ID " +
                    "    FROM SIGN_UP " +
                    "    WHERE BRANCH_ID = ? " +
                    "    )");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MemberModel model = new MemberModel(rs.getInt("member_id"),
                        rs.getDate("member_since"),
                        rs.getDate("dob"),
                        rs.getString("membership_type"),
                        rs.getString("gender").charAt(0),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public String getMembershipPriceByType(String type) {

        String returnString = null;

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT MEMBERSHIP_FEE " +
                    "FROM PRICE " +
                    "WHERE MEMBERSHIP_TYPE = ?");

            ps.setString(1, type);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                returnString = "$" + String.format("%.2f", (double) rs.getInt("membership_fee") / 100);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return returnString;
    }

    public void updateMemberEmail(int id, String email) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE member SET email = ? WHERE member_id = ?");
            ps.setString(1, email);
            ps.setInt(2, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Member " + id + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    //    division
    public ArrayList<String> membersInAllBranches() {
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT FIRST_NAME, LAST_NAME " +
                            "FROM member m " +
                            "WHERE NOT EXISTS " +
                            "(SELECT b.branch_id " +
                            "FROM branch b " +
                            "WHERE NOT EXISTS " +
                            "( SELECT r.branch_id " +
                            "FROM SIGN_UP r " +
                            "       WHERE r.branch_id = b.branch_id AND m.member_id = r.member_id))");

            while (rs.next()) {
                String fn = rs.getString("first_name") + " " + rs.getString("last_name");
                result.add(fn);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public ArrayList<MemberModel> selectMembersInProgram(int pid) {

        ArrayList<MemberModel> result = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                            "FROM MEMBER m, ATTEND a " +
                            "WHERE m.MEMBER_ID = a.MEMBER_ID and a.EVENT_ID = ?");

            ps.setInt(1, pid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MemberModel model = new MemberModel(
                        rs.getInt("member_id"),
                        rs.getDate("member_since"),
                        rs.getDate("dob"),
                        rs.getString("membership_type"),
                        rs.getString("gender").charAt(0),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));

                result.add(model);
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    public MemberModel selectMemberWithId(int mid) {

        MemberModel result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                            "FROM MEMBER m " +
                            "WHERE m.MEMBER_ID = ?");

            ps.setInt(1, mid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MemberModel model = new MemberModel(
                        rs.getInt("member_id"),
                        rs.getDate("member_since"),
                        rs.getDate("dob"),
                        rs.getString("membership_type"),
                        rs.getString("gender").charAt(0),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));

                result = model;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }
}

