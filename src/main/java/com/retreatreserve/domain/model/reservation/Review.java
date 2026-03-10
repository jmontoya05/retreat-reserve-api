package com.retreatreserve.domain.model.reservation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Review {
    private UUID id;
    private UUID userId;
    private UUID cabinId;
    private UUID reservationId;
    private Rating rating;
    private String comment;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Review() {
    }

    public Review(UUID userId, UUID cabinId, UUID reservationId, Rating rating, String comment) {
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.cabinId = Objects.requireNonNull(cabinId, "Cabin ID cannot be null");
        this.reservationId = Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        this.rating = Objects.requireNonNull(rating, "Rating cannot be null");
        this.comment = comment;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Review(UUID id, UUID userId, UUID cabinId, UUID reservationId, Rating rating,
                  String comment, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.cabinId = cabinId;
        this.reservationId = reservationId;
        this.rating = rating;
        this.comment = comment;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateReview(Rating newRating, String newComment) {
        this.rating = Objects.requireNonNull(newRating, "Rating cannot be null");
        this.comment = newComment;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateRating(Rating newRating) {
        this.rating = Objects.requireNonNull(newRating, "Rating cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    public void updateComment(String newComment) {
        this.comment = newComment;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isPositive() {
        return rating.getValue() >= 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
