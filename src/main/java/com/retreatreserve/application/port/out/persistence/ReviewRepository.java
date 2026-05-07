package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.reservation.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Review aggregate.
 */
public interface ReviewRepository {
    
    Review save(Review review);
    
    Optional<Review> findById(UUID id);
    
    List<Review> findByCabinId(UUID cabinId);
    
    List<Review> findByUserId(UUID userId);
    
    Optional<Review> findByReservationId(UUID reservationId);
    
    /**
     * Checks if a review exists for a specific reservation.
     */
    boolean existsByReservationId(UUID reservationId);
    
    /**
     * Finds all active reviews for a cabin ordered by date.
     */
    List<Review> findActiveByCabinIdOrderByCreatedAtDesc(UUID cabinId);
    
    void delete(Review review);
}
