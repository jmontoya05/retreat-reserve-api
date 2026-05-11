package com.retreatreserve.application.command.reservation;

import java.time.LocalDate;

public record CheckAvailabilityCommand(
    String cabinId,
    LocalDate checkInDate,
    LocalDate checkOutDate
) {}
