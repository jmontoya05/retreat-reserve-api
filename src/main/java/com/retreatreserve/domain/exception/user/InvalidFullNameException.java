package com.retreatreserve.domain.exception.user;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidFullNameException extends DomainException {
    public InvalidFullNameException(String message) {
        super(message);
    }
}
