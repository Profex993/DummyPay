package com.egersoft.dummypay.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private String path;
    private String errorCode;
    private Instant timestamp = Instant.now();

    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(int status, String message, String path, String errorCode) {
        this(status, message, path);
        this.errorCode = errorCode;
    }
}