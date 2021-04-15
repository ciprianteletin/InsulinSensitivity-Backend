package com.insulin.exceptions.model;

public class LinkExpiredException extends Exception{
    public LinkExpiredException(String message) {
        super(message);
    }
}
