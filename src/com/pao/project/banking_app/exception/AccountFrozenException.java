package com.pao.project.banking_app.exception;

import com.pao.project.banking_app.model.account.AccountStatus;

public class AccountFrozenException extends RuntimeException {

    public AccountFrozenException(String iban, AccountStatus status) {
        super("Cannot perform operation on account " + iban
                + ". Current status: " + status);
    }
}
