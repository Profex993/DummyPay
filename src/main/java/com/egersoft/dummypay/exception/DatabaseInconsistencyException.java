package com.egersoft.dummypay.exception;

public class DatabaseInconsistencyException extends RuntimeException {
    public DatabaseInconsistencyException(String message) {
        super(message);
    }
}
