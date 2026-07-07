package com.retreatreserve.domain.model.reservation;

import com.retreatreserve.domain.exception.reservation.InvalidGuestDetailsException;

import java.util.Objects;

import lombok.Getter;

@Getter
public class GuestDetails {
    private final Integer numberOfGuests;
    private final String guestName;
    private final String guestPhone;

    public GuestDetails(Integer numberOfGuests, String guestName, String guestPhone) {
        if (numberOfGuests == null || numberOfGuests < 1) {
            throw new InvalidGuestDetailsException("Number of guests must be at least 1");
        }
        this.numberOfGuests = numberOfGuests;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GuestDetails that = (GuestDetails) o;
        return Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }

    @Override
    public String toString() {
        return numberOfGuests + " guest(s)" + (guestName != null ? " - " + guestName : "");
    }
}
