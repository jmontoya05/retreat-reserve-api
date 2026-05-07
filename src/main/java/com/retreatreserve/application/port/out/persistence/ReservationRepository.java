package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.model.reservation.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Reservation aggregate.
 */
public interface ReservationRepository {
    
    Reservation save(Reservation reservation);
    
    Optional<Reservation> findById(UUID id);
    
    List<Reservation> findByUserId(UUID userId);
    
    List<Reservation> findByCabinId(UUID cabinId);
    
    List<Reservation> findByUserIdAndStatus(UUID userId, ReservationStatus status);
    
    /**
     * Finds active reservations for a cabin that overlap with the given date range.
     * Critical for availability checking.
     */
    List<Reservation> findOverlappingReservations(
        UUID cabinId,
        LocalDate checkInDate,
        LocalDate checkOutDate
    );
    
    /**
     * Finds reservations that need to be completed (check-out date passed).
     */
    List<Reservation> findReservationsToComplete(LocalDate currentDate);
    
    void delete(Reservation reservation);
}
