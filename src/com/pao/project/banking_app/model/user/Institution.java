package com.pao.project.banking_app.model.user;

import java.util.Objects;

public class Institution extends User implements LegalEntity {

    private String institutionName;
    private String licenseNumber;
    private String institutionType;

    public Institution(String institutionName, String licenseNumber, String institutionType,
                       String email, String phone, String address) {
        super(email, phone, address);
        this.institutionName = institutionName;
        this.licenseNumber = licenseNumber;
        this.institutionType = institutionType;
    }

    @Override
    public UserType getRole() {
        return UserType.INST;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institution other)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(licenseNumber, other.licenseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), licenseNumber);
    }

    @Override
    public String toString() {
        return "Institution{id='" + getId() + "', name='" + institutionName
                + "', license='" + licenseNumber + "', type='" + institutionType + "'}";
    }
}