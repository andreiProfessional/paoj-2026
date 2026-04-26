package com.pao.project.banking_app.model.account;

public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account"),
    CREDIT("Credit Account");

    private final String displayName;

    AccountType(String displayName) {
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
