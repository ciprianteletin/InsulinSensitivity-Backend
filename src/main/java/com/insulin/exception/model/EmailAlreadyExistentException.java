package com.insulin.exception.model;

public class EmailAlreadyExistentException extends Exception {
    public EmailAlreadyExistentException(String message) {
        super(message);
    }
}
