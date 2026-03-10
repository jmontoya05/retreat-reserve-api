package com.retreatreserve.domain.model.iam;

/**
 * User role enumeration for authorization.
 * Defines the access levels in the system.
 */
public enum Role {
    /**
     * Regular user - can browse cabins, make reservations, write reviews
     */
    USER,

    /**
     * Administrator - full access to manage cabins, users, and system
     */
    ADMIN
}
