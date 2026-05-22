package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.Role;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void shouldMapDomainToJpaEntityAndBack() {
        UUID userId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime expiry = LocalDateTime.now().plusHours(2);

        User user = new User(
            userId,
            new FullName("John", "Doe"),
            new Email("john@example.com"),
            "hashed-password",
            "+1234567890",
            Role.USER,
            true,
            "verification-token",
            expiry,
            true,
            createdAt,
            null
        );

        UserJpaEntity jpaEntity = mapper.toJpaEntity(user);

        assertNotNull(jpaEntity);
        assertEquals(userId, jpaEntity.getId());
        assertEquals("john@example.com", jpaEntity.getEmail());
        assertEquals("hashed-password", jpaEntity.getPasswordHash());
        assertEquals("+1234567890", jpaEntity.getPhoneNumber());
        assertEquals(Role.USER, jpaEntity.getRole());

        User restoredUser = mapper.toDomain(jpaEntity);

        assertNotNull(restoredUser);
        assertEquals(userId, restoredUser.getId());
        assertEquals("john@example.com", restoredUser.getEmail().getValue());
        assertEquals("John", restoredUser.getFullName().getFirstName());
        assertEquals("Doe", restoredUser.getFullName().getLastName());
        assertEquals(expiry, restoredUser.getVerificationTokenExpiry());
    }

    @Test
    void shouldReturnNullWhenMappingNullValues() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toJpaEntity(null));
    }
}
