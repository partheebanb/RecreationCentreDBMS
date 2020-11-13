package ca.ubc.cs304.model;

import java.sql.Date;

public class MemberModel {
    private final int id;
    private final Date registrationDate;
    private final Date dateOfBirth;
    private final String membershipType;
    private final char gender;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int membershipFee;

    public MemberModel(int id, Date registrationDate, Date dateOfBirth, String membershipType, char gender, String firstName, String lastName, String email, int membershipFee) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.dateOfBirth = dateOfBirth;
        this.membershipType = membershipType;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membershipFee = membershipFee;
    }

    public int getId() {
        return id;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public char getGender() {
        return gender;
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

    public int getMembershipFee() {
        return membershipFee;
    }
}
