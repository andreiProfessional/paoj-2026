package com.pao.project.banking_app.model.account;

import com.pao.project.banking_app.model.user.User;

import java.util.Objects;

public class CreditAccount extends Account {

    private double creditLimit;
    private double interestRate;
    private int billingDueDayOfMonth;

    public CreditAccount(String iban, User owner, String currency,
            double creditLimit, double interestRate,
            int billingDueDayOfMonth) {
        super(iban, owner, currency, 0.0);
        if (creditLimit <= 0) {
            throw new IllegalArgumentException("Credit limit must be positive.");
        }
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        if (billingDueDayOfMonth < 0 || billingDueDayOfMonth > 31) {
            throw new IllegalArgumentException("Billing due day must be between 0 and 31.");
        }
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.billingDueDayOfMonth = billingDueDayOfMonth;
    }

    public CreditAccount(String iban, User owner, String currency,
            double creditLimit, double interestRate) {
        this(iban, owner, currency, creditLimit, interestRate, 0);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CREDIT;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        if (creditLimit <= 0) {
            throw new IllegalArgumentException("Credit limit must be positive.");
        }
        this.creditLimit = creditLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        this.interestRate = interestRate;
    }

    public int getBillingDueDayOfMonth() {
        return billingDueDayOfMonth;
    }

    public void setBillingDueDayOfMonth(int billingDueDayOfMonth) {
        if (billingDueDayOfMonth < 0 || billingDueDayOfMonth > 31) {
            throw new IllegalArgumentException("Billing due day must be between 0 and 31.");
        }
        this.billingDueDayOfMonth = billingDueDayOfMonth;
    }

    public double getAvailableCredit() {
        return creditLimit - getBalance();
    }

    public double calculateMonthlyInterest() {
        return getBalance() * interestRate / 100.0 / 12.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CreditAccount other))
            return false;
        return super.equals(o)
                && Double.compare(creditLimit, other.creditLimit) == 0
                && Double.compare(interestRate, other.interestRate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), creditLimit, interestRate);
    }

    @Override
    public String toString() {
        return "CreditAccount{iban='" + getIban()
                + "', owner=" + getOwner().getId()
                + ", debt=" + getBalance() + " " + getCurrency()
                + ", creditLimit=" + creditLimit
                + ", availableCredit=" + getAvailableCredit()
                + ", interestRate=" + interestRate + "%"
                + ", status=" + getStatus() + "}";
    }
}
