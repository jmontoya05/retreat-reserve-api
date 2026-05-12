package com.retreatreserve.infrastructure.adapter.in.rest.mapper;

import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.infrastructure.adapter.in.rest.reservation.dto.response.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservationDtoMapper {
    
    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId().toString(),
            reservation.getUserId().toString(),
            reservation.getCabinId().toString(),
            reservation.getDateRange().getCheckInDate(),
            reservation.getDateRange().getCheckOutDate(),
            reservation.getGuestDetails().getNumberOfGuests(),
            reservation.getTotalPrice(),
            reservation.getStatus().name()
        );
    }
}
