package com.retreatreserve.application.port.in.reservation;

import com.retreatreserve.domain.model.reservation.Reservation;

public interface CompleteReservationUseCase {
    Reservation execute(String reservationId);
}
