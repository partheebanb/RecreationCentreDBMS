package ca.ubc.cs304.model;
import java.sql.Date;

public class EquipmentModel extends BookableModel {
    private final Date purchased;
    private final Date lastFixed;

    public EquipmentModel(int bookableId, String type, String name, int branchId, Date purchased, Date lastFixed) {
        super(bookableId, type, name, branchId);
        this.purchased = purchased;
        this.lastFixed = lastFixed;
    }

    public Date getPurchased() {
        return purchased;
    }

    public Date getLastFixed() {
        return lastFixed;
    }
}
