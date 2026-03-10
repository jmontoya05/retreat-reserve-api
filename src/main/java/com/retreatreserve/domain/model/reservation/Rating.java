package com.retreatreserve.domain.model.reservation;

import java.util.Objects;

import com.retreatreserve.domain.exception.reservation.InvalidRatingException;

import lombok.Getter;

@Getter
public class Rating {
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    private final Integer value;

    public Rating(Integer value) {
        if (value == null || value < MIN_RATING || value > MAX_RATING) {
            throw new InvalidRatingException(
                    "Rating must be between " + MIN_RATING + " and " + MAX_RATING + " stars"
            );
        }
        this.value = value;
    }

    public boolean isPerfect() {
        return value == MAX_RATING;
    }

    public boolean isPoor() {
        return value <= 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(value, rating.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "/5 stars";
    }
}
