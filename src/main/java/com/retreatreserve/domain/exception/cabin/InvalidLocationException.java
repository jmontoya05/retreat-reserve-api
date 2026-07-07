package com.retreatreserve.domain.exception.cabin;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidLocationException extends DomainException {
    public InvalidLocationException(String message) {
        super(message);
    }
}
