package com.retreatreserve.application.service.reservation;

import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.Capacity;
import com.retreatreserve.domain.model.cabin.Location;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.Role;
import com.retreatreserve.domain.model.iam.User;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CabinRepository cabinRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CancelReservationService cancelReservationService;

    @Test
    void shouldCancelReservationAndSendCancellationEmail() {
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

        User user = new User(
            userId,
            new FullName("Jane", "Smith"),
            new Email("jane@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            true,
            null,
            null,
            true,
            LocalDateTime.now().minusDays(20),
            null
        );

        Cabin cabin = new Cabin(
            cabinId,
            "Lake House",
            "Beautiful retreat",
            UUID.randomUUID(),
            new Location("City", "State", "Country"),
            new Capacity(4),
            2,
            2,
            new BigDecimal("250"),
            BigDecimal.ZERO,
            0,
            null,
            true,
            null,
            null,
            null,
            LocalDateTime.now().minusDays(20),
            null
        );

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cabinRepository.findById(cabinId)).thenReturn(Optional.of(cabin));

        Reservation result = cancelReservationService.execute(reservationId.toString());

        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
        verify(emailService).sendReservationCancellation(
            user.getEmail().getValue(),
            user.getFullName().getFullName(),
            cabin.getName()
        );
    }

    @Test
    void shouldCancelReservationWithoutEmailIfUserOrCabinMissing() {
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
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(cabinRepository.findById(cabinId)).thenReturn(Optional.empty());

        Reservation result = cancelReservationService.execute(reservationId.toString());

        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
    }
}
