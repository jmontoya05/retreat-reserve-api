package com.retreatreserve.application.port.in.reservation;

import com.retreatreserve.application.command.reservation.CheckAvailabilityCommand;

public interface CheckAvailabilityUseCase {
    boolean execute(CheckAvailabilityCommand command);
}
