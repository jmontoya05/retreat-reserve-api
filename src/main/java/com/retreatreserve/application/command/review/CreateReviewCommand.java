package com.retreatreserve.application.command.review;

public record CreateReviewCommand(
    String userId,
    String cabinId,
    String reservationId,
    Integer rating,
    String comment
) {}
