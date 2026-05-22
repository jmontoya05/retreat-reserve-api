package com.retreatreserve.application.service.review;

import com.retreatreserve.application.command.review.CreateReviewCommand;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.application.port.out.persistence.ReviewRepository;
import com.retreatreserve.domain.exception.reservation.CannotReviewException;
import com.retreatreserve.domain.exception.reservation.ReviewAlreadyExistsException;
import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.GuestDetails;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.model.reservation.ReservationStatus;
import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.domain.service.RatingCalculationService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RatingCalculationService ratingCalculationService;

    @InjectMocks
    private CreateReviewService createReviewService;

    @Test
    void shouldCreateReviewSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        CreateReviewCommand command = new CreateReviewCommand(
            userId.toString(), reservationId.toString(), 5, "Lovely stay"
        );

        Reservation reservation = new Reservation(
            reservationId,
            userId,
            cabinId,
            DateRange.reconstitute(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)),
            new GuestDetails(2, "Guest", "+1234567890"),
            new BigDecimal("650"),
            ReservationStatus.COMPLETED,
            "No special requests",
            true,
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(5)
        );

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reviewRepository.existsByReservationId(reservationId)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review result = createReviewService.execute(command);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(cabinId, result.getCabinId());
        assertEquals(reservationId, result.getReservationId());
        assertEquals(5, result.getRating().getValue());

        verify(ratingCalculationService).updateCabinRating(cabinId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void shouldThrowExceptionWhenReservationNotCompleted() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        CreateReviewCommand command = new CreateReviewCommand(
            userId.toString(), reservationId.toString(), 4, "Nice"
        );

        Reservation reservation = new Reservation(
            reservationId,
            userId,
            cabinId,
            new DateRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)),
            new GuestDetails(2, "Guest", "+1234567890"),
            new BigDecimal("650"),
            ReservationStatus.CONFIRMED,
            null,
            true,
            LocalDateTime.now(),
            null
        );

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThrows(CannotReviewException.class, () -> createReviewService.execute(command));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenReviewAlreadyExists() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        CreateReviewCommand command = new CreateReviewCommand(
            userId.toString(), reservationId.toString(), 4, "Nice"
        );

        Reservation reservation = new Reservation(
            reservationId,
            userId,
            cabinId,
            DateRange.reconstitute(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)),
            new GuestDetails(2, "Guest", "+1234567890"),
            new BigDecimal("650"),
            ReservationStatus.COMPLETED,
            null,
            true,
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(5)
        );

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reviewRepository.existsByReservationId(reservationId)).thenReturn(true);

        assertThrows(ReviewAlreadyExistsException.class, () -> createReviewService.execute(command));
        verify(reviewRepository, never()).save(any());
    }
}
