package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.port.out.persistence.ReservationRepository;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserReservationsServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private GetUserReservationsService getUserReservationsService;

    @Test
    void shouldReturnReservationsForUser() {
        UUID userId = UUID.randomUUID();
        Reservation reservation = new Reservation(
            UUID.randomUUID(),
            userId,
            UUID.randomUUID(),
            DateRange.reconstitute(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)),
            new GuestDetails(2, "John Doe", "+1234567890"),
            new BigDecimal("1200"),
            ReservationStatus.CONFIRMED,
            null,
            true,
            LocalDateTime.now().minusDays(15),
            null
        );

        when(reservationRepository.findByUserId(userId)).thenReturn(List.of(reservation));

        List<Reservation> result = getUserReservationsService.execute(userId.toString());

        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }
}
