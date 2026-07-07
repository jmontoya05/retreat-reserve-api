package com.retreatreserve.domain.model.cabin;

import com.retreatreserve.domain.exception.cabin.InvalidLocationException;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;

@Getter
public class Location {
    private final String city;
    private final String state;
    private final String country;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Location(String city, String state, String country) {
        if (city == null || city.isBlank())
            throw new InvalidLocationException("City cannot be empty");
        if (state == null || state.isBlank())
            throw new InvalidLocationException("State cannot be empty");
        if (country == null || country.isBlank())
            throw new InvalidLocationException("Country cannot be empty");
        this.city = city.trim();
        this.state = state.trim();
        this.country = country.trim();
    }

    public Location(String city, String state, String country, String address,
            BigDecimal latitude, BigDecimal longitude) {
        this(city, state, country);
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFullLocation() {
        return city + ", " + state + ", " + country;
    }

    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Location location = (Location) o;
        return Objects.equals(city, location.city) &&
                Objects.equals(state, location.state) &&
                Objects.equals(country, location.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, state, country);
    }
}
