package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class CannotCancelCompletedReservationException extends DomainException {
    public CannotCancelCompletedReservationException(String message) {
        super(message);
    }
}
