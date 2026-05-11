package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class FeatureAlreadyExistsException extends DomainException {
    public FeatureAlreadyExistsException(String message) {
        super(message);
    }
}
