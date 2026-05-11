package com.retreatreserve.application.port.in.reservation;

import java.util.List;

import com.retreatreserve.domain.model.reservation.Reservation;

public interface GetUserReservationsUseCase {
    List<Reservation> execute(String userId);
}
