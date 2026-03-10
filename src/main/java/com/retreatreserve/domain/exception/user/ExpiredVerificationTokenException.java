package com.retreatreserve.domain.exception.user;

import com.retreatreserve.domain.exception.DomainException;

public class ExpiredVerificationTokenException extends DomainException {
    public ExpiredVerificationTokenException(String message) {
        super(message);
    }
}
