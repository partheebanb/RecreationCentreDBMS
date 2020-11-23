package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Time;

public class BookingModel {
    private final int id;
    private final Date date;
    private final java.sql.Time time;
    private final int memberId;
    private final int branchId;

    public BookingModel(int id, Date date, Time time, int memberId, int branchId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.memberId = memberId;
        this.branchId = branchId;
    }

    public BookingModel(int id, Date date, int memberId, int branchId) {
        this.id = id;
        this.date = date;
        this.time = null;
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

    public Time getTime() {
        return time;
    }
}
