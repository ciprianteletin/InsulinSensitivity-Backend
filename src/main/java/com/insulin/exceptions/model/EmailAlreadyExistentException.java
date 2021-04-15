package com.insulin.exceptions.model;

public class EmailAlreadyExistentException extends Exception {
    public EmailAlreadyExistentException(String message) {
        super(message);
    }
}
