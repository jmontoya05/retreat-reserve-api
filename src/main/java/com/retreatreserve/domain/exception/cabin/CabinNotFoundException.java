package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class CabinNotFoundException extends DomainException {
    public CabinNotFoundException(String message) {
        super(message);
    }
}
