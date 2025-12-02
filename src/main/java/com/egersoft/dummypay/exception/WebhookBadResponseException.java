package com.egersoft.dummypay.exception;

public class WebhookBadResponseException extends RuntimeException {
    public WebhookBadResponseException(String message) {
        super(message);
    }
}
