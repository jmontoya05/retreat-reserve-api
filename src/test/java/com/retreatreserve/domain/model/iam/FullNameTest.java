package com.retreatreserve.domain.model.iam;

import com.retreatreserve.domain.exception.user.InvalidFullNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FullNameTest {

    @Test
    void shouldThrowInvalidFullNameExceptionWhenFirstNameIsBlank() {
        assertThrows(InvalidFullNameException.class, () -> new FullName(" ", "Doe"));
    }
}
