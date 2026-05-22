package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.UpdateUserProfileCommand;
import com.retreatreserve.application.port.out.persistence.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserProfileService updateUserProfileService;

    @Test
    void shouldUpdateProfileWhenUserExists() {
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
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
            userId.toString(), "Jane", "Smith", "+1987654321"
        );

        User result = updateUserProfileService.execute(command);

        assertEquals("Jane", result.getFullName().getFirstName());
        assertEquals("Smith", result.getFullName().getLastName());
        assertEquals("+1987654321", result.getPhoneNumber());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
            userId.toString(), "Jane", "Smith", "+1987654321"
        );

        assertThrows(UserNotFoundException.class, () -> updateUserProfileService.execute(command));
    }
}
