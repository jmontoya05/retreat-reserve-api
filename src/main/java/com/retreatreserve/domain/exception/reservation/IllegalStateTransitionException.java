package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class IllegalStateTransitionException extends DomainException {
    public IllegalStateTransitionException(String message) {
        super(message);
    }
}
