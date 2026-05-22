package com.retreatreserve.application.service.iam;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdService getUserByIdService;

    @Test
    void shouldReturnUserWhenFound() {
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

        User result = getUserByIdService.execute(userId.toString());

        assertEquals(user, result);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        String userIdStr = userId.toString();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getUserByIdService.execute(userIdStr));
    }
}
