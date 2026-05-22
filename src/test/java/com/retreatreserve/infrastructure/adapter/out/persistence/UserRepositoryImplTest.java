package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.Role;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.out.persistence.embeddable.FullNameEmbeddable;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.UserJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Test
    void shouldSaveUserAndReturnSavedDomainModel() {
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            new FullName("John", "Doe"),
            new Email("john@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            true,
            "token",
            LocalDateTime.now().plusHours(2),
            true,
            LocalDateTime.now().minusDays(1),
            null
        );

        UserJpaEntity jpaEntity = new UserJpaEntity();
        jpaEntity.setId(userId);
        jpaEntity.setFullName(new FullNameEmbeddable("John", "Doe"));
        jpaEntity.setEmail("john@example.com");
        jpaEntity.setPasswordHash("hashed-password");
        jpaEntity.setPhoneNumber("+1234567890");
        jpaEntity.setRole(Role.USER);
        jpaEntity.setEmailVerified(true);
        jpaEntity.setVerificationToken("token");
        jpaEntity.setVerificationTokenExpiry(LocalDateTime.now().plusHours(2));
        jpaEntity.setActive(true);
        jpaEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        jpaEntity.setUpdatedAt(null);

        UserJpaEntity savedEntity = new UserJpaEntity();
        savedEntity.setId(userId);

        when(mapper.toJpaEntity(user)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(user);

        User result = userRepository.save(user);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("john@example.com", result.getEmail().getValue());
        verify(jpaRepository).save(jpaEntity);
    }

    @Test
    void shouldFindByEmailWhenUserExists() {
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

        UserJpaEntity savedEntity = new UserJpaEntity();
        when(jpaRepository.findByEmail("john@example.com")).thenReturn(Optional.of(savedEntity));
        when(mapper.toDomain(savedEntity)).thenReturn(user);

        Optional<User> result = userRepository.findByEmail(new Email("john@example.com"));

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void shouldDeleteUserById() {
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

        userRepository.delete(user);

        verify(jpaRepository).deleteById(userId);
    }
}
