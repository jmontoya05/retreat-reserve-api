package com.retreatreserve.domain.model.iam;

import com.retreatreserve.domain.exception.user.ExpiredVerificationTokenException;
import com.retreatreserve.domain.exception.user.InvalidVerificationTokenException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void verifyEmailSuccessAndExpiryCleared() {
        User user = new User(
            UUID.randomUUID(),
            new FullName("A", "B"),
            new Email("a@b.com"),
            "hash",
            "+1",
            Role.USER,
            false,
            "token-abc",
            LocalDateTime.now().plusHours(2),
            true,
            LocalDateTime.now(),
            null
        );

        user.verifyEmail("token-abc");
        assertTrue(user.getEmailVerified());
        assertNull(user.getVerificationToken());
    }

    @Test
    void verifyEmailInvalidToken() {
        User user = new User(
            UUID.randomUUID(),
            new FullName("A", "B"),
            new Email("a@b.com"),
            "hash",
            "+1",
            Role.USER,
            false,
            "token-abc",
            LocalDateTime.now().plusHours(2),
            true,
            LocalDateTime.now(),
            null
        );

        assertThrows(InvalidVerificationTokenException.class, () -> user.verifyEmail("wrong-token"));
    }

    @Test
    void verifyEmailExpiredToken() {
        User user = new User(
            UUID.randomUUID(),
            new FullName("A", "B"),
            new Email("a@b.com"),
            "hash",
            "+1",
            Role.USER,
            false,
            "token-abc",
            LocalDateTime.now().minusHours(1),
            true,
            LocalDateTime.now(),
            null
        );

        assertThrows(ExpiredVerificationTokenException.class, () -> user.verifyEmail("token-abc"));
    }

    @Test
    void updatePasswordAndProfileAndRoleTransitions() {
        User user = new User(new FullName("First","Last"), new Email("x@y.com"), "oldhash", "+1");

        user.updatePassword("newhash");
        assertEquals("newhash", user.getPasswordHash());

        user.updateProfile(new FullName("F2","L2"), "+2");
        assertEquals("F2", user.getFullName().getFirstName());
        assertEquals("+2", user.getPhoneNumber());

        user.promoteToAdmin();
        assertTrue(user.isAdmin());

        user.demoteToUser();
        assertFalse(user.isAdmin());

        user.deactivate();
        assertFalse(user.getActive());

        user.activate();
        assertTrue(user.getActive());
    }
}
