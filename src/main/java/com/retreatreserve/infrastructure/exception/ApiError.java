package com.retreatreserve.infrastructure.exception;

import java.time.LocalDateTime;

public record ApiError(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {
    public ApiError(int status, String error, String message) {
        this(status, error, message, LocalDateTime.now());
    }
}
