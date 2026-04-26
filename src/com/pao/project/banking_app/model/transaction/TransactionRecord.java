package com.pao.project.banking_app.model.transaction;

import com.pao.project.banking_app.model.account.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionRecord(
        String id,
        Account fromAccount,
        Account toAccount,
        double amount,
        double fee,
        String currency,
        TransactionType type,
        LocalDateTime timestamp) {

    public TransactionRecord {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }
        if (fee < 0) {
            throw new IllegalArgumentException("Transaction fee cannot be negative.");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null.");
        }
        if (fromAccount == null && toAccount == null) {
            throw new IllegalArgumentException("At least one of fromAccount or toAccount must be set.");
        }
    }

    public static TransactionRecord deposit(Account toAccount, double amount, double fee, String currency) {
        return new TransactionRecord(
                UUID.randomUUID().toString(), null, toAccount,
                amount, fee, currency, TransactionType.DEPOSIT, LocalDateTime.now());
    }

    public static TransactionRecord withdrawal(Account fromAccount, double amount, double fee, String currency) {
        return new TransactionRecord(
                UUID.randomUUID().toString(), fromAccount, null,
                amount, fee, currency, TransactionType.WITHDRAWAL, LocalDateTime.now());
    }

    public static TransactionRecord transfer(Account fromAccount, Account toAccount, double amount, double fee,
            String currency) {
        return new TransactionRecord(
                UUID.randomUUID().toString(), fromAccount, toAccount,
                amount, fee, currency, TransactionType.TRANSFER, LocalDateTime.now());
    }

    public static TransactionRecord payment(Account fromAccount, Account toAccount, double amount, double fee,
            String currency) {
        return new TransactionRecord(
                UUID.randomUUID().toString(), fromAccount, toAccount,
                amount, fee, currency, TransactionType.PAYMENT, LocalDateTime.now());
    }

    public double totalCost() {
        return amount + fee;
    }

    @Override
    public String toString() {
        return "TransactionRecord{id='" + id
                + "', type=" + type
                + ", from=" + (fromAccount != null ? fromAccount.getIban() : "-")
                + ", to=" + (toAccount != null ? toAccount.getIban() : "-")
                + ", amount=" + amount + " " + currency
                + ", fee=" + fee
                + ", timestamp=" + timestamp + "}";
    }
}
