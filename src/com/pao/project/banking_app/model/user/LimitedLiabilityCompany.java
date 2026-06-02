package com.pao.project.banking_app.model.user;

import java.util.Objects;

public class LimitedLiabilityCompany extends Company {

    private double socialCapital;
    private int associateCount;

    public LimitedLiabilityCompany(String companyName, String urc, String industry,
               double socialCapital, int associateCount,
               String email, String phone, String address) {
        super(companyName, urc, industry, email, phone, address);
        this.socialCapital = socialCapital;
        this.associateCount = associateCount;
    }

    @Override
    public UserType getRole() {
        return UserType.LLC;
    }

    public double getSocialCapital() {
        return socialCapital;
    }

    public void setSocialCapital(double socialCapital) {
        if (socialCapital < 0) {
            throw new IllegalArgumentException("Social capital cannot be negative.");
        }
        this.socialCapital = socialCapital;
    }

    public int getAssociateCount() {
        return associateCount;
    }

    public void setAssociateCount(int associateCount) {
        if (associateCount < 1) {
            throw new IllegalArgumentException("Associate count must be at least 1.");
        }
        this.associateCount = associateCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LimitedLiabilityCompany other)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUrc(), other.getUrc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), socialCapital);
    }

    @Override
    public String toString() {
        return "SRL{id='" + getId() + "', name='" + getCompanyName() + "', urc='" + getUrc()
                + "', socialCapital=" + socialCapital + ", associates=" + associateCount + "}";
    }
}