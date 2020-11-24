package ca.ubc.cs304.database;

import ca.ubc.cs304.model.EmployeeModel;
import ca.ubc.cs304.model.ReserveRelation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeHandler {
    private static final String EXCEPTION_TAG = "[RESERVE HANDLER EXCEPTION]";
    private static final String WARNING_TAG = "[RESERVE HANDLER WARNING]";

    Connection connection;

    public EmployeeHandler(Connection connection) {
        this.connection = connection;
    }

    // Get all employee working on a specific branch
    public ArrayList<EmployeeModel> getEmployeeByBranch(int branchId) {
        ArrayList<EmployeeModel> employeeModels = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * " +
                        "FROM EMPLOYEE " +
                        "WHERE BRANCH_ID = ?"
            );
            ps.setInt(1, branchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EmployeeModel employeeModel = new EmployeeModel(
                    rs.getInt("employee_id"),
                    rs.getString("employee_first_name"),
                    rs.getString("employee_last_name"),
                    rs.getString("employee_email"),
                    rs.getInt("employee_sin"),
                    rs.getDate("employee_dob"),
                    rs.getString("employee_gender").charAt(0),
                    rs.getString("employee_address"),
                    rs.getInt("branch_id")
                );

                employeeModels.add(employeeModel);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return employeeModels;
    }
}
