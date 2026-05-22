package com.retreatreserve.domain.model.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.retreatreserve.domain.exception.reservation.InvalidDateRangeException;

import lombok.Getter;

@Getter
public class DateRange {
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    // Used for new reservations — enforces future dates
    public DateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new InvalidDateRangeException("Dates cannot be null");
        }

        if (checkInDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new InvalidDateRangeException(
                    "Check-in must be at least 1 day in advance. Minimum date: " + LocalDate.now().plusDays(1)
            );
        }

        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            throw new InvalidDateRangeException("Check-out date must be after check-in date");
        }

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Used when rehydrating existing reservations from persistence (no future-date requirement)
    private DateRange(LocalDate checkInDate, LocalDate checkOutDate, boolean skipFutureDateValidation) {
        if (checkInDate == null || checkOutDate == null)
            throw new InvalidDateRangeException("Dates cannot be null");
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate))
            throw new InvalidDateRangeException("Check-out date must be after check-in date");

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public static DateRange reconstitute(LocalDate checkInDate, LocalDate checkOutDate) {
        return new DateRange(checkInDate, checkOutDate, true);
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public boolean overlapsWith(DateRange other) {
        if (other == null) return false;
        return !this.checkOutDate.isBefore(other.checkInDate) &&
                !this.checkInDate.isAfter(other.checkOutDate);
    }

    public boolean contains(LocalDate date) {
        if (date == null) return false;
        return !date.isBefore(checkInDate) && !date.isAfter(checkOutDate);
    }

    public boolean isPastCheckOut() {
        return LocalDate.now().isAfter(checkOutDate);
    }

    public boolean isPastCheckIn() {
        return LocalDate.now().isAfter(checkInDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(checkInDate, dateRange.checkInDate) &&
                Objects.equals(checkOutDate, dateRange.checkOutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkInDate, checkOutDate);
    }

    @Override
    public String toString() {
        return checkInDate + " to " + checkOutDate + " (" + getNumberOfNights() + " nights)";
    }
}
