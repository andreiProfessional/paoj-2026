package com.pao.project.banking_app.model.user;

public enum UserType {
    PERS("Simple Person"),
    AUTH_IND("Authorized Individual"),
    LLC("Limited Liability Company"),
    INC("Incorporation"),
    INST("Institution");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}