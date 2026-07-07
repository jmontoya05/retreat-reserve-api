package com.retreatreserve.domain.model.iam;

import com.retreatreserve.domain.exception.user.InvalidFullNameException;

import java.util.Objects;

import lombok.Getter;

/**
 * Value Object representing a person's full name.
 */
@Getter
public class FullName {
    private final String firstName;
    private final String lastName;

    /**
     * Creates a new FullName value object.
     */
    public FullName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new InvalidFullNameException("First name cannot be empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidFullNameException("Last name cannot be empty");
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    /**
     * Returns the full name as a single string.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FullName fullName = (FullName) o;
        return Objects.equals(firstName, fullName.firstName) &&
                Objects.equals(lastName, fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
