package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidRatingException extends DomainException {
    public InvalidRatingException(String message) {
        super(message);
    }
}
