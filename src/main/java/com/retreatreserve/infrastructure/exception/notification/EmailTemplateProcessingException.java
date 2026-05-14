package com.retreatreserve.infrastructure.exception.notification;

public class EmailTemplateProcessingException extends RuntimeException {
        public EmailTemplateProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
