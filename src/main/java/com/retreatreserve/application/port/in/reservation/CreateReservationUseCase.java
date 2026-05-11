package com.retreatreserve.application.port.in.reservation;

import com.retreatreserve.application.command.reservation.CreateReservationCommand;
import com.retreatreserve.domain.model.reservation.Reservation;

public interface CreateReservationUseCase {
    Reservation execute(CreateReservationCommand command);
}
