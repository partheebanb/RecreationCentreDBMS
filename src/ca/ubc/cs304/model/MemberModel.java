package ca.ubc.cs304.model;

import java.sql.Date;

public class MemberModel implements Comparable {
    private final int id;
    private final Date registrationDate;
    private final Date dateOfBirth;
    private final String membershipType;
    private final char gender;
    private final String firstName;
    private final String lastName;
    private final String email;

    public MemberModel(int id, Date registrationDate, Date dateOfBirth, String membershipType, char gender, String firstName, String lastName, String email) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.dateOfBirth = dateOfBirth;
        this.membershipType = membershipType;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getGender() {
        switch (gender) {
            case 'F':
            case 'f':
                return "Female";
            case 'M':
            case 'm':
                return "Male";
        }
        return "Prefer not to say";
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

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof MemberModel) {
            return Integer.compare(this.id, ((MemberModel) o).id);
        }
        return 0;
    }
}
