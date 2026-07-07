package com.retreatreserve.application.exception.storage;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
