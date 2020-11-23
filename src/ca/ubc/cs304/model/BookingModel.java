package ca.ubc.cs304.model;

import java.sql.Date;

public class BookingModel {
    private final int id;
    private final Date date;
    private final int memberId;
    private final int branchId;

    public BookingModel(int id, Date date, int memberId, int branchId) {
        this.id = id;
        this.date = date;
        this.memberId = memberId;
        this.branchId = branchId;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getBranchId() {
        return branchId;
    }
}
