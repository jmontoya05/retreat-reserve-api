package com.retreatreserve.domain.model.cabin;

/**
 * Cabin status enumeration.
 * Defines the operational state of a cabin.
 */
public enum CabinStatus {
    /**
     * Cabin is available for booking
     */
    AVAILABLE,

    /**
     * Cabin is temporarily disabled by admin
     */
    DISABLED,

    /**
     * Cabin is under maintenance and cannot be booked
     */
    UNDER_MAINTENANCE
}
