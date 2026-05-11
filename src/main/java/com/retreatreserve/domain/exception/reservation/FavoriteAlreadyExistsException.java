package com.retreatreserve.domain.exception.reservation;

import com.retreatreserve.domain.exception.DomainException;

public class FavoriteAlreadyExistsException extends DomainException {
    public FavoriteAlreadyExistsException(String message) {
        super(message);
    }
}
