package com.retreatreserve.infrastructure.adapter.in.rest.reservation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationResponse(
    String id,
    String userId,
    String cabinId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Integer numberOfGuests,
    BigDecimal totalPrice,
    String status
) {}
