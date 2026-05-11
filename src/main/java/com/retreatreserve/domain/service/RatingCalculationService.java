package com.retreatreserve.domain.service;

import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.application.port.out.persistence.ReviewRepository;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.reservation.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

/**
 * Domain service for calculating cabin ratings.
 */
@Service
@RequiredArgsConstructor
public class RatingCalculationService {
    
    private final ReviewRepository reviewRepository;
    private final CabinRepository cabinRepository;
    
    /**
     * Recalculates and updates cabin rating after a review is added/updated/deleted.
     */
    @Transactional
    public void updateCabinRating(UUID cabinId) {
        List<Review> reviews = reviewRepository.findActiveByCabinIdOrderByCreatedAtDesc(cabinId);
        
        if (reviews.isEmpty()) {
            Cabin cabin = cabinRepository.findById(cabinId)
                .orElseThrow(() -> new IllegalArgumentException("Cabin not found"));
            cabin.updateRatingSummary(BigDecimal.ZERO, 0);
            cabinRepository.save(cabin);
            return;
        }
        
        double sum = reviews.stream()
            .mapToInt(r -> r.getRating().getValue())
            .sum();
        
        BigDecimal average = BigDecimal.valueOf(sum / reviews.size())
            .setScale(2, RoundingMode.HALF_UP);
        
        Cabin cabin = cabinRepository.findById(cabinId)
            .orElseThrow(() -> new IllegalArgumentException("Cabin not found"));
        cabin.updateRatingSummary(average, reviews.size());
        cabinRepository.save(cabin);
    }
}
