package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.UserJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of UserRepository port.
 * Bridges domain layer with JPA infrastructure.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;
    
    @Override
    @Transactional
    public User save(User user) {
        UserJpaEntity jpaEntity = mapper.toJpaEntity(user);
        UserJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByVerificationToken(String token) {
        return jpaRepository.findByVerificationToken(token)
                .map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }
    
    @Override
    @Transactional
    public void delete(User user) {
        jpaRepository.deleteById(user.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Iterable<User> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
