package com.retreatreserve.domain.model.reservation;

import com.retreatreserve.domain.exception.reservation.InvalidRatingException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RatingTest {
    
    @Test
    void shouldCreateValidRating() {
        Rating rating = new Rating(5);
        assertEquals(5, rating.getValue());
    }
    
    @Test
    void shouldThrowExceptionForInvalidRating() {
        assertThrows(InvalidRatingException.class, () -> new Rating(0));
        assertThrows(InvalidRatingException.class, () -> new Rating(6));
        assertThrows(InvalidRatingException.class, () -> new Rating(-1));
    }
    
    @Test
    void shouldAcceptAllValidRatings() {
        for (int i = 1; i <= 5; i++) {
            Rating rating = new Rating(i);
            assertEquals(i, rating.getValue());
        }
    }
}
