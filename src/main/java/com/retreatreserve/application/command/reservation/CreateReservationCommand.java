package com.retreatreserve.application.command.reservation;

import java.time.LocalDate;

public record CreateReservationCommand(
    String userId,
    String cabinId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Integer numberOfGuests,
    String guestName,
    String guestPhone,
    String specialRequests
) {
    public CreateReservationCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (cabinId == null || cabinId.isBlank()) {
            throw new IllegalArgumentException("Cabin ID is required");
        }
        if (checkInDate == null) {
            throw new IllegalArgumentException("Check-in date is required");
        }
        if (checkOutDate == null) {
            throw new IllegalArgumentException("Check-out date is required");
        }
        if (numberOfGuests == null || numberOfGuests < 1) {
            throw new IllegalArgumentException("Number of guests must be at least 1");
        }
    }
}
