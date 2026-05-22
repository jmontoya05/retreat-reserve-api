package com.retreatreserve.domain.model.reservation;

import com.retreatreserve.domain.exception.reservation.CannotCancelCompletedReservationException;
import com.retreatreserve.domain.exception.reservation.CannotCompleteBeforeCheckoutException;
import com.retreatreserve.domain.exception.reservation.IllegalStateTransitionException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void cannotCancelCompletedReservation() {
        Reservation r = new Reservation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            DateRange.reconstitute(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)),
            new GuestDetails(2, "G", "+1"),
            new BigDecimal("200"),
            ReservationStatus.COMPLETED,
            null,
            true,
            LocalDateTime.now().minusDays(10),
            null
        );

        assertThrows(CannotCancelCompletedReservationException.class, r::cancel);
    }

    @Test
    void cannotCompleteBeforeCheckout() {
        Reservation r = new Reservation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            DateRange.reconstitute(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)),
            new GuestDetails(2, "G", "+1"),
            new BigDecimal("200"),
            ReservationStatus.CONFIRMED,
            null,
            true,
            LocalDateTime.now().minusDays(2),
            null
        );

        assertThrows(CannotCompleteBeforeCheckoutException.class, r::complete);
    }

    @Test
    void confirmIllegalState() {
        Reservation r = new Reservation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            DateRange.reconstitute(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)),
            new GuestDetails(2, "G", "+1"),
            new BigDecimal("200"),
            ReservationStatus.CONFIRMED,
            null,
            true,
            LocalDateTime.now().minusDays(10),
            null
        );

        assertThrows(IllegalStateTransitionException.class, r::confirm);
    }
}
