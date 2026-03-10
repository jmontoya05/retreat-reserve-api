package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
