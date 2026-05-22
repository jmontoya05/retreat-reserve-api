package com.retreatreserve.domain.service;

import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.domain.exception.cabin.CabinNotAvailableException;
import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Domain service for checking cabin availability.
 * This is critical business logic that prevents double bookings.
 */
@Service
@RequiredArgsConstructor
public class AvailabilityService {
    
    private final ReservationRepository reservationRepository;
    
    /**
     * Checks if a cabin is available for the given date range.
     * Throws exception if cabin is not available.
     */
    public void ensureCabinAvailable(UUID cabinId, DateRange dateRange) {
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
            cabinId,
            dateRange.getCheckInDate(),
            dateRange.getCheckOutDate()
        );
        
        if (!overlapping.isEmpty()) {
            throw new CabinNotAvailableException(
                "Cabin is already booked for the selected dates. " +
                "Found " + overlapping.size() + " overlapping reservation(s)."
            );
        }
    }
    
    /**
     * Checks availability without throwing exception.
     * Returns true if available, false otherwise.
     */
    public boolean isAvailable(UUID cabinId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
            cabinId,
            checkIn,
            checkOut
        );
        
        return overlapping.isEmpty();
    }
}
