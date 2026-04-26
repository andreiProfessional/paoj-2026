package com.pao.project.banking_app.model.account;

import com.pao.project.banking_app.model.user.User;

import java.time.LocalDate;
import java.util.Objects;

public class SavingsAccount extends Account {

    private double interestRate;
    private LocalDate maturityDate;

    public SavingsAccount(String iban, User owner, String currency,
            double initialBalance, double interestRate,
            LocalDate maturityDate) {
        super(iban, owner, currency, initialBalance);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        this.interestRate = interestRate;
        this.maturityDate = maturityDate;
    }

    public SavingsAccount(String iban, User owner, String currency,
            double initialBalance, double interestRate) {
        this(iban, owner, currency, initialBalance, interestRate, null);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
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

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public boolean isMatured() {
        return maturityDate != null && !LocalDate.now().isBefore(maturityDate);
    }

    public double calculateAnnualInterest() {
        return getBalance() * interestRate / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SavingsAccount other))
            return false;
        return super.equals(o)
                && Double.compare(interestRate, other.interestRate) == 0
                && Objects.equals(maturityDate, other.maturityDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), interestRate, maturityDate);
    }

    @Override
    public String toString() {
        return "SavingsAccount{iban='" + getIban()
                + "', owner=" + getOwner().getId()
                + ", balance=" + getBalance() + " " + getCurrency()
                + ", interestRate=" + interestRate + "%"
                + ", maturityDate=" + (maturityDate != null ? maturityDate : "open-ended")
                + ", status=" + getStatus() + "}";
    }
}
