package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;

public class ProgramHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public ProgramHandler(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<String> getProgramByMemberID(int mid) {
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                            "FROM ATTEND a, MEMBER m, PROGRAM p " +
                            "WHERE a.MEMBER_ID = m.MEMBER_ID " +
                            "    AND a.EVENT_ID = p.EVENT_ID " +
                            "    AND m.MEMBER_ID = ?");

            ps.setInt(1, mid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(
                        rs.getString("name") + " from " +
                        rs.getDate("start_date").toString());
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }
}



