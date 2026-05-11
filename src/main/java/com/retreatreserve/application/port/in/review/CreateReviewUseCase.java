package com.retreatreserve.application.port.in.review;

import com.retreatreserve.application.command.review.CreateReviewCommand;
import com.retreatreserve.domain.model.reservation.Review;

public interface CreateReviewUseCase {
    Review execute(CreateReviewCommand command);
}
