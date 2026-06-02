package com.pao.project.banking_app.model.user;

import java.util.Objects;

public class Incorporation extends Company {

    private double shareCapital;
    private int boardMemberCount;

    public Incorporation(String companyName, String urc, String industry,
                         double shareCapital, int boardMemberCount,
                         String email, String phone, String address) {
        super(companyName, urc, industry, email, phone, address);
        this.shareCapital = shareCapital;
        this.boardMemberCount = boardMemberCount;
    }

    @Override
    public UserType getRole() {
        return UserType.INC;
    }

    public double getShareCapital() {
        return shareCapital;
    }

    public void setShareCapital(double shareCapital) {
        if (shareCapital < 0) {
            throw new IllegalArgumentException("Share capital cannot be negative.");
        }
        this.shareCapital = shareCapital;
    }

    public int getBoardMemberCount() {
        return boardMemberCount;
    }

    public void setBoardMemberCount(int boardMemberCount) {
        this.boardMemberCount = boardMemberCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Incorporation other)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(shareCapital, other.shareCapital) == 0
                && Objects.equals(getUrc(), other.getUrc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shareCapital);
    }

    @Override
    public String toString() {
        return "SA{id='" + getId() + "', name='" + getCompanyName() + "', urc='" + getUrc()
                + "', shareCapital=" + shareCapital + ", boardMembers=" + boardMemberCount + "}";
    }
}
