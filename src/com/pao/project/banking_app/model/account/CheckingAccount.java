package com.pao.project.banking_app.model.account;

import com.pao.project.banking_app.model.user.User;

import java.util.Objects;

public class CheckingAccount extends Account {

    private double overdraftLimit;

    public CheckingAccount(String iban, User owner, String currency,
            double initialBalance, double overdraftLimit) {
        super(iban, owner, currency, initialBalance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(String iban, User owner, String currency, double initialBalance) {
        this(iban, owner, currency, initialBalance, 0.0);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getAvailableBalance() {
        return getBalance() + overdraftLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CheckingAccount other))
            return false;
        return super.equals(o)
                && Double.compare(overdraftLimit, other.overdraftLimit) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), overdraftLimit);
    }

    @Override
    public String toString() {
        return "CheckingAccount{iban='" + getIban()
                + "', owner=" + getOwner().getId()
                + ", balance=" + getBalance() + " " + getCurrency()
                + ", overdraftLimit=" + overdraftLimit
                + ", status=" + getStatus() + "}";
    }
}
