package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class PriceModel {
    private final int membershipFee;
    private final String membershipType;
    private final java.sql.Date startDate;
    private final java.sql.Date endDate;
	
	public PriceModel(int membershipFee, String membershipType) {
        this.membershipFee = membershipFee;
        this.membershipType = membershipType;
        
	}

	public String getMemberShipFee() {
        return membershipFee;
        
    public String getMembershipType() {
        return membershipType
    }
    
