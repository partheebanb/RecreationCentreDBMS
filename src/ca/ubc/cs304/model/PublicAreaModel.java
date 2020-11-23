package ca.ubc.cs304.model;
import java.sql.Date;

public class PublicAreaModel {
    private final int areaId;
    private final String name;
    private final String type;
    private final boolean isOutdoor;
    private final int branchId;

    public PublicAreaModel(int areaId, String name, String type, boolean isOutdoor, int branchId) {
        this.areaId = areaId;
        this.name = name;
        this.type = type;
        this.isOutdoor = isOutdoor;
        this.branchId = branchId;
    }

    public int getAreaId() {
        return areaId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isOutdoor() {
        return isOutdoor;
    }

    public int getBranchId() {return branchId; };

}
