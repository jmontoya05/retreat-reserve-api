package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class FeatureNotFoundException extends DomainException {
    public FeatureNotFoundException(String message) {
        super(message);
    }
}
