package com.retreatreserve.domain.model.iam;

import java.util.Objects;
import java.util.regex.Pattern;

import com.retreatreserve.domain.exception.user.InvalidEmailException;

import lombok.Getter;

/**
 * Value Object representing an email address.
 * Ensures email validity and immutability.
 */
@Getter
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String value;

    /**
     * Creates a new Email value object.
     *
     * @param value the email address
     * @throws InvalidEmailException if email format is invalid
     */
    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("Email cannot be empty");
        }

        String trimmedEmail = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new InvalidEmailException("Invalid email format: " + value);
        }

        this.value = trimmedEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
