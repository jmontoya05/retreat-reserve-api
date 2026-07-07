package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidGuestDetailsException extends DomainException {
    public InvalidGuestDetailsException(String message) {
        super(message);
    }
}
