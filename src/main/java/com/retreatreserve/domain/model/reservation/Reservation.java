package com.retreatreserve.domain.model.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.retreatreserve.domain.exception.reservation.CannotCancelCompletedReservationException;
import com.retreatreserve.domain.exception.reservation.CannotCompleteBeforeCheckoutException;
import com.retreatreserve.domain.exception.reservation.IllegalStateTransitionException;
import com.retreatreserve.domain.exception.reservation.ReservationAlreadyCancelledException;

import lombok.Getter;

/**
 * Reservation entity - Aggregate Root for booking management.
 */
@Getter
public class Reservation {
    private UUID id;
    private UUID userId;     // Reference by ID only
    private UUID cabinId;    // Reference by ID only
    private DateRange dateRange;
    private GuestDetails guestDetails;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    private String specialRequests;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Reservation() {
    }

    /**
     * Creates a new Reservation.
     */
    public Reservation(UUID userId, UUID cabinId, DateRange dateRange,
                       GuestDetails guestDetails, BigDecimal totalPrice) {
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.cabinId = Objects.requireNonNull(cabinId, "Cabin ID cannot be null");
        this.dateRange = Objects.requireNonNull(dateRange, "Date range cannot be null");
        this.guestDetails = Objects.requireNonNull(guestDetails, "Guest details cannot be null");
        this.totalPrice = Objects.requireNonNull(totalPrice, "Total price cannot be null");
        this.status = ReservationStatus.CONFIRMED;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Reconstitutes a Reservation from persistence.
     */
    public Reservation(UUID id, UUID userId, UUID cabinId, DateRange dateRange,
                       GuestDetails guestDetails, BigDecimal totalPrice, ReservationStatus status,
                       String specialRequests, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.cabinId = cabinId;
        this.dateRange = dateRange;
        this.guestDetails = guestDetails;
        this.totalPrice = totalPrice;
        this.status = status;
        this.specialRequests = specialRequests;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Domain Methods - State Transitions ==========

    public void cancel() {
        if (this.status == ReservationStatus.COMPLETED) {
            throw new CannotCancelCompletedReservationException("Cannot cancel a completed reservation");
        }

        if (this.status == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException("Reservation is already cancelled");
        }

        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateTransitionException(
                    "Can only complete CONFIRMED reservations. Current status: " + this.status
            );
        }

        if (LocalDate.now().isBefore(dateRange.getCheckOutDate())) {
            throw new CannotCompleteBeforeCheckoutException(
                    "Cannot complete reservation before check-out date: " + dateRange.getCheckOutDate()
            );
        }

        this.status = ReservationStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateTransitionException(
                    "Can only confirm PENDING reservations. Current status: " + this.status
            );
        }

        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Domain Methods - Business Logic ==========

    public boolean overlapsWith(DateRange other) {
        return this.dateRange.overlapsWith(other);
    }

    public boolean isActiveReservation() {
        return (this.status == ReservationStatus.CONFIRMED ||
                this.status == ReservationStatus.PENDING) &&
                this.active;
    }

    public boolean canBeReviewed() {
        return this.status == ReservationStatus.COMPLETED;
    }

    public boolean canBeCancelled() {
        return this.status != ReservationStatus.COMPLETED &&
                this.status != ReservationStatus.CANCELLED;
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Equals & HashCode ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
