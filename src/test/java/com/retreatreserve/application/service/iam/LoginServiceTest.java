package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.dto.iam.LoginResult;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.application.port.out.security.AuthenticationService;
import com.retreatreserve.application.port.out.security.TokenProvider;
import com.retreatreserve.domain.exception.user.InvalidCredentialsException;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.Role;
import com.retreatreserve.domain.model.iam.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationManager;

    @Mock
    private TokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginService loginService;

    @Test
    void shouldLoginSuccessfully() {
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            new FullName("John", "Doe"),
            new Email("john@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            true,
            null,
            null,
            true,
            LocalDateTime.now().minusDays(1),
            null
        );

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(userId.toString())).thenReturn("jwt-token");

        LoginResult result = loginService.execute("john@example.com", "password123");

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        assertEquals(user, result.user());

        verify(authenticationManager).authenticate(userId.toString(), "password123");
    }

    @Test
    void shouldThrowInvalidCredentialsWhenAuthenticationFails() {
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            new FullName("John", "Doe"),
            new Email("john@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            true,
            null,
            null,
            true,
            LocalDateTime.now().minusDays(1),
            null
        );

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
        doThrow(new BadCredentialsException("bad credentials"))
            .when(authenticationManager).authenticate(anyString(), anyString());

        assertThrows(InvalidCredentialsException.class,
            () -> loginService.execute("john@example.com", "wrong-password"));
    }

    @Test
    void shouldThrowUserNotFoundWhenEmailDoesNotExist() {
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
            () -> loginService.execute("missing@example.com", "password123"));
    }
}
