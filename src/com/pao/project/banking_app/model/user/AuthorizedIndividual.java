package com.pao.project.banking_app.model.user;

import java.util.Objects;

public class AuthorizedIndividual extends User implements NaturalPerson {

    private String firstName;
    private String lastName;
    private String ssn;
    private String authorizationCode;
    private String activityDomain;

    public AuthorizedIndividual(String firstName, String lastName, String ssn,
               String authorizationCode, String activityDomain,
               String email, String phone, String address) {
        super(email, phone, address);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.authorizationCode = authorizationCode;
        this.activityDomain = activityDomain;
    }

    @Override
    public UserType getRole() {
        return UserType.AUTH_IND;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getActivityDomain() {
        return activityDomain;
    }

    public void setActivityDomain(String activityDomain) {
        this.activityDomain = activityDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizedIndividual other)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(authorizationCode, other.authorizationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authorizationCode);
    }

    @Override
    public String toString() {
        return "PFA{id='" + getId() + "', name='" + getFullName() + "', authCode='" + authorizationCode + "', domain='" + activityDomain + "'}";
    }
}