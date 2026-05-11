package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class CategoryAlreadyExistsException extends DomainException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
