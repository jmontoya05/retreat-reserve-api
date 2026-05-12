package com.retreatreserve.application.service.review;

import com.retreatreserve.application.command.review.CreateReviewCommand;
import com.retreatreserve.application.port.in.review.CreateReviewUseCase;
import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.application.port.out.persistence.ReviewRepository;
import com.retreatreserve.domain.exception.reservation.ReservationNotFoundException;
import com.retreatreserve.domain.exception.reservation.ReviewAlreadyExistsException;
import com.retreatreserve.domain.exception.reservation.CannotReviewException;
import com.retreatreserve.domain.model.reservation.Rating;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.domain.service.RatingCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateReviewService implements CreateReviewUseCase {
    
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final RatingCalculationService ratingCalculationService;
    
    @Override
    @Transactional
    public Review execute(CreateReviewCommand command) {
        log.info("Creating review for reservation: {}", command.reservationId());
        
        UUID reservationId = UUID.fromString(command.reservationId());
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        
        if (!reservation.canBeReviewed()) {
            throw new CannotReviewException("Reservation must be completed before reviewing");
        }
        
        if (reviewRepository.existsByReservationId(reservationId)) {
            throw new ReviewAlreadyExistsException("Reservation already reviewed");
        }
        
        Rating rating = new Rating(command.rating());
        Review review = new Review(
            UUID.fromString(command.userId()),
            UUID.fromString(reservation.getCabinId().toString()),
            reservationId,
            rating,
            command.comment()
        );
        
        Review savedReview = reviewRepository.save(review);
        
        ratingCalculationService.updateCabinRating(UUID.fromString(reservation.getCabinId().toString()));
        
        log.info("Review created: {}", savedReview.getId());
        return savedReview;
    }
}
