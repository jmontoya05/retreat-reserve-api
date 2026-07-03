package com.retreatreserve.application.service.review;

import com.retreatreserve.application.port.in.review.GetCabinReviewsUseCase;
import com.retreatreserve.application.port.out.persistence.ReviewRepository;
import com.retreatreserve.domain.model.reservation.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCabinReviewsService implements GetCabinReviewsUseCase {
    
    private final ReviewRepository reviewRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Review> execute(String cabinId) {
        UUID parsedCabinId = UUID.fromString(cabinId);
        return reviewRepository.findActiveByCabinIdOrderByCreatedAtDesc(parsedCabinId);
    }
}
