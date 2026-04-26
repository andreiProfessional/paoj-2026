package com.pao.project.banking_app.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String iban) {
        super("Account not found with IBAN: " + iban);
    }
}
