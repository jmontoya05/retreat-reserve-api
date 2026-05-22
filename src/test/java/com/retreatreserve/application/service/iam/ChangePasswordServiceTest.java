package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.ChangePasswordCommand;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.application.port.out.security.PasswordEncoder;
import com.retreatreserve.domain.exception.user.InvalidPasswordException;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ChangePasswordService changePasswordService;

    @Test
    void shouldChangePasswordWhenCurrentPasswordMatches() {
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
            LocalDateTime.now(),
            null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpass", "hashed-password")).thenReturn(true);
        when(passwordEncoder.encode("newpass")).thenReturn("new-hash");
        when(userRepository.save(user)).thenReturn(user);

        changePasswordService.execute(new ChangePasswordCommand(userId.toString(), "oldpass", "newpass"));

        assertEquals("new-hash", user.getPasswordHash());
    }

    @Test
    void shouldThrowWhenCurrentPasswordIsIncorrect() {
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
            LocalDateTime.now(),
            null
        );
        ChangePasswordCommand command = new ChangePasswordCommand(userId.toString(), "wrong", "newpass");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () ->
            changePasswordService.execute(command)
        );
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        ChangePasswordCommand command = new ChangePasswordCommand(userId.toString(), "oldpass", "newpass");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
            changePasswordService.execute(command)
        );
    }
}
