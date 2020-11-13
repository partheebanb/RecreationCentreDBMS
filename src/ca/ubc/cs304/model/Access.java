package ca.ubc.cs304.model;

import java.sql.Date;

public class Access {
    private final int memberId;
    private final int publicAreaId;
    private final Date date;

    public Access(int memberId, int publicAreaId, Date date) {
        this.memberId = memberId;
        this.publicAreaId = publicAreaId;
        this.date = date;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getPublicAreaId() {
        return publicAreaId;
    }

    public Date getDate() {
        return date;
    }
}
