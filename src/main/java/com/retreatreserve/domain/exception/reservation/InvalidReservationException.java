package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class InvalidReservationException extends DomainException {
    public InvalidReservationException(String message) {
        super(message);
    }
}
