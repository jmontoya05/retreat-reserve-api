package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.domain.exception.reservation.ReservationNotFoundException;
import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.GuestDetails;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.model.reservation.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompleteReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CompleteReservationService completeReservationService;

    @Test
    void shouldCompleteReservationWhenCheckoutHasPassed() {
        UUID reservationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        Reservation reservation = new Reservation(
            reservationId,
            userId,
            cabinId,
            DateRange.reconstitute(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)),
            new GuestDetails(2, "John Doe", "+1234567890"),
            new BigDecimal("1000"),
            ReservationStatus.CONFIRMED,
            null,
            true,
            LocalDateTime.now().minusDays(10),
            null
        );

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = completeReservationService.execute(reservationId.toString());

        assertEquals(ReservationStatus.COMPLETED, result.getStatus());
    }

    @Test
    void shouldThrowWhenReservationNotFound() {
        UUID reservationId = UUID.randomUUID();
        String reservationIdStr = reservationId.toString();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
            () -> completeReservationService.execute(reservationIdStr)
        );
    }
}
