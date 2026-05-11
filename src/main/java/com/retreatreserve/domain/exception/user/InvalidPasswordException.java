package com.retreatreserve.domain.exception.user;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidPasswordException extends DomainException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
