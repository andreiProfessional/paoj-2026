package com.pao.project.banking_app.service;

import com.pao.project.banking_app.model.account.Account;
import com.pao.project.banking_app.model.transaction.TransactionRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransactionService {

    private static TransactionService instance;

    private final List<TransactionRecord> transactions = new ArrayList<>();

    private TransactionService() {
    }

    public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public void record(TransactionRecord transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        transactions.add(transaction);
    }

    public Optional<TransactionRecord> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or blank.");
        }
        return transactions.stream()
                .filter(t -> t.id().equals(id))
                .findFirst();
    }

    public List<TransactionRecord> findByAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        List<TransactionRecord> result = new ArrayList<>();
        for (TransactionRecord t : transactions) {
            if (account.equals(t.fromAccount()) || account.equals(t.toAccount())) {
                result.add(t);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public List<TransactionRecord> listAll() {
        return Collections.unmodifiableList(transactions);
    }
}
