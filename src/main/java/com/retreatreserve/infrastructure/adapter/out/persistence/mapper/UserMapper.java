package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.out.persistence.embeddable.FullNameEmbeddable;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between User domain model and UserJpaEntity.
 * Handles conversion of value objects and maintains clean separation.
 */
@Component
public class UserMapper {
    
    /**
     * Converts JPA entity to domain model.
     */
    public User toDomain(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        FullName fullName = new FullName(
            jpaEntity.getFullName().getFirstName(),
            jpaEntity.getFullName().getLastName()
        );
        
        Email email = new Email(jpaEntity.getEmail());
        
        return new User(
            jpaEntity.getId(),
            fullName,
            email,
            jpaEntity.getPasswordHash(),
            jpaEntity.getPhoneNumber(),
            jpaEntity.getRole(),
            jpaEntity.getEmailVerified(),
            jpaEntity.getVerificationToken(),
            jpaEntity.getVerificationTokenExpiry(),
            jpaEntity.getActive(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    /**
     * Converts domain model to JPA entity (for new entities).
     */
    public UserJpaEntity toJpaEntity(User domain) {
        if (domain == null) {
            return null;
        }
        
        UserJpaEntity jpaEntity = new UserJpaEntity();
        updateJpaEntity(domain, jpaEntity);
        
        return jpaEntity;
    }
    
    /**
     * Updates existing JPA entity from domain model (for updates).
     */
    public void updateJpaEntity(User domain, UserJpaEntity jpaEntity) {
        if (domain == null || jpaEntity == null) {
            return;
        }
        
        jpaEntity.setId(domain.getId());
        jpaEntity.setFullName(new FullNameEmbeddable(
            domain.getFullName().getFirstName(),
            domain.getFullName().getLastName()
        ));
        jpaEntity.setEmail(domain.getEmail().getValue());
        jpaEntity.setPasswordHash(domain.getPasswordHash());
        jpaEntity.setPhoneNumber(domain.getPhoneNumber());
        jpaEntity.setRole(domain.getRole());
        jpaEntity.setEmailVerified(domain.getEmailVerified());
        jpaEntity.setVerificationToken(domain.getVerificationToken());
        jpaEntity.setVerificationTokenExpiry(domain.getVerificationTokenExpiry());
        jpaEntity.setActive(domain.getActive());
        jpaEntity.setCreatedAt(domain.getCreatedAt());
        jpaEntity.setUpdatedAt(domain.getUpdatedAt());
    }
}
