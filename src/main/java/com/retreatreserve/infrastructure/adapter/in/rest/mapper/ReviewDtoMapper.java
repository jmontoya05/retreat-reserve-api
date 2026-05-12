package com.retreatreserve.infrastructure.adapter.in.rest.mapper;

import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.infrastructure.adapter.in.rest.review.dto.response.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewDtoMapper {
    
    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
            review.getId().toString(),
            review.getUserId().toString(),
            review.getCabinId().toString(),
            review.getRating().getValue(),
            review.getComment(),
            review.getCreatedAt()
        );
    }
}
