package ca.ubc.cs304.model;

import java.awt.print.Book;

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

    @Override
    public String toString() {
        return getName() + " " + getBookableId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookableModel) {
            return ((BookableModel) obj).getBookableId() == this.getBookableId();
        }
        return false;
    }
}
