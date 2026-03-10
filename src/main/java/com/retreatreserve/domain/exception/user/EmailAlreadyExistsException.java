package com.retreatreserve.domain.exception.user;

import com.retreatreserve.domain.exception.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
