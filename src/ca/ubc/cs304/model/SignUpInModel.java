package ca.ubc.cs304.model;

public class SignUpInModel {
    private final int member_id;
    private final int branch_id;

    public SignUpInModel(int member_id, int branch_id) {
        this.member_id = member_id;
        this.branch_id = branch_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public int getBranch_id() {
        return branch_id;
    }
}
