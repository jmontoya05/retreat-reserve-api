package com.retreatreserve.application.service.review;

import com.retreatreserve.application.port.out.persistence.ReviewRepository;
import com.retreatreserve.domain.model.reservation.Rating;
import com.retreatreserve.domain.model.reservation.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCabinReviewsServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private GetCabinReviewsService getCabinReviewsService;

    @Test
    void shouldReturnCabinReviews() {
        UUID cabinId = UUID.randomUUID();
        Review review = new Review(
            UUID.randomUUID(),
            UUID.randomUUID(),
            cabinId,
            UUID.randomUUID(),
            new Rating(5),
            "Great stay",
            true,
            LocalDateTime.now(),
            null
        );

        when(reviewRepository.findActiveByCabinIdOrderByCreatedAtDesc(cabinId)).thenReturn(List.of(review));

        List<Review> result = getCabinReviewsService.execute(cabinId.toString());

        assertEquals(1, result.size());
        assertEquals(review, result.get(0));
    }

    @Test
    void shouldThrowExceptionWhenCabinIdIsNotAValidUuid() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> getCabinReviewsService.execute("not-a-valid-uuid")
        );

        assertEquals("Invalid UUID string: not-a-valid-uuid", exception.getMessage());
        verifyNoInteractions(reviewRepository);
    }
}
