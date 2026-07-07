package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidCapacityException extends DomainException {
    public InvalidCapacityException(String message) {
        super(message);
    }
}
