package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class CannotReviewException extends DomainException {
    public CannotReviewException(String message) {
        super(message);
    }
}
