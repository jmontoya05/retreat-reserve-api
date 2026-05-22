package com.retreatreserve.domain.model.iam;

import org.junit.jupiter.api.Test;

import com.retreatreserve.domain.exception.user.InvalidEmailException;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    
    @Test
    void shouldCreateValidEmail() {
        Email email = new Email("user@example.com");
        assertEquals("user@example.com", email.getValue());
    }
    
    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> new Email("invalid-email"));
        assertThrows(InvalidEmailException.class, () -> new Email(""));
        assertThrows(InvalidEmailException.class, () -> new Email(null));
    }
    
    @Test
    void shouldBeEqualForSameEmail() {
        Email email1 = new Email("user@example.com");
        Email email2 = new Email("user@example.com");
        assertEquals(email1, email2);
    }
}
