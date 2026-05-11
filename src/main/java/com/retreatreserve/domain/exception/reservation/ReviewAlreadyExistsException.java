package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class ReviewAlreadyExistsException extends DomainException {
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
