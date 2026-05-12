package com.retreatreserve.infrastructure.adapter.in.rest.review;

import com.retreatreserve.application.command.review.CreateReviewCommand;
import com.retreatreserve.application.port.in.review.CreateReviewUseCase;
import com.retreatreserve.application.port.in.review.GetCabinReviewsUseCase;
import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.infrastructure.adapter.in.rest.review.dto.request.CreateReviewRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.review.dto.response.ReviewResponse;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.ReviewDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final CreateReviewUseCase createReviewUseCase;
    private final GetCabinReviewsUseCase getCabinReviewsUseCase;
    private final ReviewDtoMapper reviewDtoMapper;
    
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @RequestHeader("X-User-Id") String userId) {
        
        CreateReviewCommand command = new CreateReviewCommand(
            userId, request.reservationId(),
            request.rating(), request.comment()
        );
        
        Review review = createReviewUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDtoMapper.toResponse(review));
    }
    
    @GetMapping("/cabin/{cabinId}")
    public ResponseEntity<List<ReviewResponse>> getCabinReviews(@PathVariable String cabinId) {
        List<Review> reviews = getCabinReviewsUseCase.execute(cabinId);
        return ResponseEntity.ok(
            reviews.stream()
                .map(reviewDtoMapper::toResponse)
                .collect(Collectors.toList())
        );
    }
}
