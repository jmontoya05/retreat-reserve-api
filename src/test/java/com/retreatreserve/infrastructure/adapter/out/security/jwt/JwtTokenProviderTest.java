package com.retreatreserve.infrastructure.adapter.out.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "very-secure-secret-key-that-is-at-least-32-chars");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpiration", 3600000L);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String userId = "d8b6c5f2-5f6d-4f8b-8a2d-4f3f4dc9003c";
        String token = tokenProvider.generateToken(userId);

        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals(userId, tokenProvider.getUserIdFromToken(token));
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        assertFalse(tokenProvider.validateToken(invalidToken));
    }
}
