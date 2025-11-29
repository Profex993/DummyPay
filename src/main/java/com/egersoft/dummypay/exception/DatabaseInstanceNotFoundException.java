package com.egersoft.dummypay.exception;

public class DatabaseInstanceNotFoundException extends RuntimeException {
    public DatabaseInstanceNotFoundException(String message) {
        super(message);
    }
}
