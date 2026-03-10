package com.retreatreserve.domain.model.cabin;

import java.util.Objects;

import lombok.Getter;

@Getter
public class Capacity {
    private final Integer maxGuests;

    public Capacity(Integer maxGuests) {
        if (maxGuests == null || maxGuests < 1) {
            throw new IllegalArgumentException("Max guests must be at least 1");
        }
        this.maxGuests = maxGuests;
    }

    public boolean canAccommodate(Integer numberOfGuests) {
        return numberOfGuests != null && numberOfGuests <= maxGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capacity capacity = (Capacity) o;
        return Objects.equals(maxGuests, capacity.maxGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxGuests);
    }

    @Override
    public String toString() {
        return maxGuests + " guests";
    }
}
