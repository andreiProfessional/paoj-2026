package com.pao.project.banking_app.model.user;

import java.util.Objects;

public abstract class Company extends User implements LegalEntity {

    private String companyName;
    private String urc;
    private String industry;

    protected Company(String companyName, String urc, String industry,
                      String email, String phone, String address) {
        super(email, phone, address);
        this.companyName = companyName;
        this.urc = urc;
        this.industry = industry;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUrc() {
        return urc;
    }

    public void setUrc(String urc) {
        this.urc = urc;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company other)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(urc, other.urc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), urc);
    }

    @Override
    public String toString() {
        return "Company{id='" + getId() + "', name='" + companyName + "', urc='" + urc + "', role=" + getRole() + "}";
    }
}