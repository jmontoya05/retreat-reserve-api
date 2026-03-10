package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class ReservationAlreadyCancelledException extends DomainException {
    public ReservationAlreadyCancelledException(String message) {
        super(message);
    }
}
