package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class ReviewNotFoundException extends DomainException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
