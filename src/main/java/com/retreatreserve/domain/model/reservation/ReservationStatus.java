package com.retreatreserve.domain.model.reservation;

/**
 * Reservation status enumeration.
 * Defines the lifecycle states of a reservation.
 */
public enum ReservationStatus {
    /**
     * Reservation is pending confirmation (future feature)
     */
    PENDING,

    /**
     * Reservation is confirmed and active
     */
    CONFIRMED,

    /**
     * Reservation has been cancelled
     */
    CANCELLED,

    /**
     * Reservation is completed (check-out date has passed)
     */
    COMPLETED
}
