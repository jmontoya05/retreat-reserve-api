package com.retreatreserve.domain.exception.user;

import com.retreatreserve.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
