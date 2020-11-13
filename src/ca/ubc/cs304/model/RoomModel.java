package ca.ubc.cs304.model;
import java.sql.Date;

public class RoomModel extends BookableModel {
    private final Date lastInnovation;

    public RoomModel(int bookableId, String type, String name, int branchId, Date lastInnovation) {
        super(bookableId, type, name, branchId);
        this.lastInnovation = lastInnovation;
    }
}
