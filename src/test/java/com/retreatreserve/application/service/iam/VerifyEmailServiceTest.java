package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.VerifyEmailCommand;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.exception.user.InvalidVerificationTokenException;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.Role;
import com.retreatreserve.domain.model.iam.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifyEmailServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VerifyEmailService verifyEmailService;

    @Test
    void shouldVerifyEmailWhenTokenIsValid() {
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            new FullName("John", "Doe"),
            new Email("john@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            false,
            "token-123",
            LocalDateTime.now().plusHours(2),
            true,
            LocalDateTime.now(),
            null
        );

        when(userRepository.findByVerificationToken("token-123")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenAnswer(invocation -> invocation.getArgument(0));

        User result = verifyEmailService.execute(new VerifyEmailCommand("token-123"));

        assertTrue(result.getEmailVerified());
        assertNull(result.getVerificationToken());
        assertNull(result.getVerificationTokenExpiry());
    }

    @Test
    void shouldThrowWhenTokenIsInvalid() {
        VerifyEmailCommand command = new VerifyEmailCommand("invalid-token");

        when(userRepository.findByVerificationToken(command.token())).thenReturn(Optional.empty());

        assertThrows(InvalidVerificationTokenException.class,
            () -> verifyEmailService.execute(command)
        );
    }
}
