package ca.ubc.cs304.database;

import ca.ubc.cs304.model.AccessRelation;
import ca.ubc.cs304.model.MemberModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccessHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public AccessHandler(Connection connection) {
        this.connection = connection;
    }

    // Get the accesses for a specific branch.
    // Format the data based on how we will display it in the tables.
    public ArrayList<ArrayList<String>> getAccessDataInBranch(int branch_id) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT m.FIRST_NAME, m.LAST_NAME, p.AREA_NAME, a.ACCESS_DATE " +
                            "FROM \"ACCESS\" a, MEMBER m, PUBLIC_AREA p " +
                            "WHERE a.MEMBER_ID = m.MEMBER_ID " +
                            "  AND p.AREA_ID = a.AREA_ID " +
                            "  AND p.BRANCH_ID = ?");

            ps.setInt(1, branch_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ArrayList<String> s = new ArrayList<>();
                s.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                s.add(rs.getString("area_name"));
                s.add(rs.getDate("access_date").toString());
                s.add(rs.getTime("access_date").toString());

                result.add(s);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    // Get the accesses for a specific member.
    // Format the data based on how we will display it in the tables.
    public ArrayList<ArrayList<String>> getAccessDataForMember(int memberId) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT b.BRANCH_NAME, p.AREA_NAME, a.ACCESS_DATE " +
                        "FROM \"ACCESS\" a, PUBLIC_AREA p, BRANCH b " +
                        "WHERE b.BRANCH_ID = p.BRANCH_ID " +
                        "  AND p.AREA_ID = a.AREA_ID " +
                        "  AND a.MEMBER_ID = ?");

            ps.setInt(1, memberId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ArrayList<String> s = new ArrayList<>();
                s.add(rs.getString("branch_name"));
                s.add(rs.getString("area_name"));
                s.add(rs.getDate("access_date").toString());
                s.add(rs.getTime("access_date").toString());

                result.add(s);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void insertAccess(AccessRelation accessRelation) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"ACCESS\"  (MEMBER_ID, AREA_ID, ACCESS_DATE) " +
                    "VALUES (?,?,?)");
            ps.setInt(1, accessRelation.getMemberId());
            ps.setDate(2, accessRelation.getDate());
            ps.setInt(3, accessRelation.getMemberId());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // AGGREGATION with GROUP BY, HAVING
    // Find users that have accessed public areas before and their # of accesses
    public Map<MemberModel, Integer> avgAccessGroupedByDateHaving(int leastAccess) {
        Map<MemberModel, Integer> resultMap = new HashMap<>();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                            "FROM MEMBER M JOIN ( " +
                            "    SELECT a.MEMBER_ID, COUNT(a.MEMBER_ID) " +
                            "    FROM \"ACCESS\" a " +
                            "    GROUP BY a.MEMBER_ID " +
                            "    HAVING COUNT(a.MEMBER_ID) >= ? " +
                            ") G on M.MEMBER_ID = G.MEMBER_ID");

            ps.setInt(1, leastAccess);
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

                resultMap.put(model, rs.getInt("count(a.member_id)"));
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return resultMap;
    }
}
