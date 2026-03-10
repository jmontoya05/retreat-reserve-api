package com.retreatreserve.domain.exception.user;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidVerificationTokenException extends DomainException {
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
