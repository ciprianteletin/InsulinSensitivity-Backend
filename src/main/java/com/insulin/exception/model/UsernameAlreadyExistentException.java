package com.insulin.exception.model;

public class UsernameAlreadyExistentException extends Exception {
    public UsernameAlreadyExistentException(String message) {
        super(message);
    }
}
