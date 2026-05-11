package com.retreatreserve.application.port.in.review;

import java.util.List;

import com.retreatreserve.domain.model.reservation.Review;

public interface GetCabinReviewsUseCase {
    List<Review> execute(String cabinId);
}
