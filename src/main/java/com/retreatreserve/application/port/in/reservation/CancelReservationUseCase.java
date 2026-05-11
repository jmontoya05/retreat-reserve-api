package com.retreatreserve.application.port.in.reservation;

import com.retreatreserve.domain.model.reservation.Reservation;

public interface CancelReservationUseCase {
    Reservation execute(String reservationId);
}
