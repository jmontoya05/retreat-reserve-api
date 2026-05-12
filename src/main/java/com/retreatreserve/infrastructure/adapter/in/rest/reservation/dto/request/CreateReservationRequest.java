package com.retreatreserve.infrastructure.adapter.in.rest.reservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateReservationRequest(
    @NotBlank(message = "Cabin ID is required")
    String cabinId,

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    LocalDate checkInDate,

    @NotNull(message = "Check-out date is required")
    @Future LocalDate checkOutDate,

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "At least one guest is required") 
    Integer numberOfGuests,

    String guestName,

    String guestPhone,
    
    String specialRequests
) {}
