package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class CannotCompleteBeforeCheckoutException extends DomainException {
    public CannotCompleteBeforeCheckoutException(String message) {
        super(message);
    }
}
