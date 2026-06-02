package com.pao.project.banking_app.model.user;

import java.util.Objects;
import java.util.UUID;

public abstract class User {

    private final String id;
    private String email;
    private String phone;
    private String address;

    protected User(String email, String phone, String address) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public abstract UserType getRole();

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', role=" + getRole() + ", email='" + email + "'}";
    }
}