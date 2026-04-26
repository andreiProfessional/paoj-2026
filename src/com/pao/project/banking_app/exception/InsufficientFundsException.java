package com.pao.project.banking_app.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String iban, double requested, double available) {
        super("Insufficient funds in account " + iban
                + ". Requested: " + requested
                + ", Available: " + available);
    }
}
