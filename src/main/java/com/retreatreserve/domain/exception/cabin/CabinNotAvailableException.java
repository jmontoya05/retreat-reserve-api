package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class CabinNotAvailableException extends DomainException {
    public CabinNotAvailableException(String message) {
        super(message);
    }
}
