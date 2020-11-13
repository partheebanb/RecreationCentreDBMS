package ca.ubc.cs304.model;

public class BookableModel {
    private final int bookableId;
    private final String type;
    private final String name;
    private final int branchId;

    public BookableModel(int bookableId, String type, String name, int branchId) {
        this.bookableId = bookableId;
        this.type = type;
        this.name = name;
        this.branchId = branchId;
    }

    public int getBookableId() {
        return bookableId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getBranchId() {
        return branchId;
    }
}
