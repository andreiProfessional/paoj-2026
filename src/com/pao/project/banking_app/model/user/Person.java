package com.pao.project.banking_app.model.user;

import java.util.Objects;

public class Person extends User implements NaturalPerson {

    private String firstName;
    private String lastName;
    private String ssn;

    public Person(String firstName, String lastName, String ssn,
                  String email, String phone, String address) {
        super(email, phone, address);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    @Override
    public UserType getRole() {
        return UserType.PERS;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person other)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(ssn, other.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ssn);
    }

    @Override
    public String toString() {
        return "Person{id='" + getId() + "', name='" + getFullName() + "', ssn='" + ssn + "', email='" + getEmail() + "'}";
    }
}