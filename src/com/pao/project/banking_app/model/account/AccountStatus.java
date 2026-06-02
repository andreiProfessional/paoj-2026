package com.pao.project.banking_app.model.account;

public enum AccountStatus {
    ACTIVE("Active"),
    FROZEN("Frozen"),
    CLOSED("Closed");

    private final String displayName;

    AccountStatus(String displayName) {
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
