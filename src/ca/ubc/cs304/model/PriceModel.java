package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class PriceModel {
    private final int membershipFee;
    private final String membershipType;
    private final Date startDate;
    private final Date endDate;

    public PriceModel(int membershipFee, String membershipType, Date startDate, Date endDate) {
        this.membershipFee = membershipFee;
        this.membershipType = membershipType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getMembershipFee() {
        return membershipFee;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
