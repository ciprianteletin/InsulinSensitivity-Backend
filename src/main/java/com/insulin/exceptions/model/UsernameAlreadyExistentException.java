package com.insulin.exceptions.model;

public class UsernameAlreadyExistentException extends Exception {
    public UsernameAlreadyExistentException(String message) {
        super(message);
    }
}
