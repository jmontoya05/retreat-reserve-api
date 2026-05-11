package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class ExceedsCapacityException extends DomainException {
    public ExceedsCapacityException(String message) {
        super(message);
    }
}
