package com.retreatreserve.infrastructure.adapter.in.rest.review.dto.response;

import java.time.LocalDateTime;

public record ReviewResponse(
    String id,
    String userId,
    String cabinId,
    Integer rating,
    String comment,
    LocalDateTime createdAt
) {}
