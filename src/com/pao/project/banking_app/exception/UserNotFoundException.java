package com.pao.project.banking_app.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
