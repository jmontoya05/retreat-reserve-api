package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class DuplicateFeatureException extends DomainException {
    public DuplicateFeatureException(String message) {
        super(message);
    }
}
