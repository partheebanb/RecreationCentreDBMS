package ca.ubc.cs304.model;
import java.sql.Date;

public class EmployeeModel {
    private final int employeeId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int sin;
    private final Date dob;
    private final char gender;
    private final String address;
    private final int branchId;

    public EmployeeModel(int employeeId, String firstName, String lastName, String email, int sin, Date dob, char gender, String address, int branchId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sin = sin;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.branchId = branchId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getSin() {
        return sin;
    }

    public Date getDob() {
        return dob;
    }

    public char getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public int getBranchId() {
        return branchId;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
