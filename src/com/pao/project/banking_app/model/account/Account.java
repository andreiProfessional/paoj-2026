package com.pao.project.banking_app.model.account;

import com.pao.project.banking_app.model.user.User;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Account implements Comparable<Account> {

    private final String iban;
    private final User owner;
    private final LocalDate openedAt;
    private double balance;
    private String currency;
    private AccountStatus status;

    protected Account(String iban, User owner, String currency, double initialBalance) {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN cannot be null or blank.");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Account must have an owner.");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.iban = iban;
        this.owner = owner;
        this.currency = currency;
        this.balance = initialBalance;
        this.openedAt = LocalDate.now();
        this.status = AccountStatus.ACTIVE;
    }

    public abstract AccountType getAccountType();

    public String getIban() {
        return iban;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDate getOpenedAt() {
        return openedAt;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank.");
        }
        this.currency = currency;
    }

    public void setStatus(AccountStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    public void credit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive.");
        }
        this.balance += amount;
    }

    public void debit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive.");
        }
        this.balance -= amount;
    }

    @Override
    public int compareTo(Account other) {
        return this.iban.compareTo(other.iban);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Account other))
            return false;
        return Objects.equals(iban, other.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return "Account{iban='" + iban + "', type=" + getAccountType()
                + ", owner=" + owner.getId()
                + ", balance=" + balance + " " + currency
                + ", status=" + status + ", openedAt=" + openedAt + "}";
    }
}
