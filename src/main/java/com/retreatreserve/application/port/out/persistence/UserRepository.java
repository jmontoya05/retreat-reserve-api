package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for User aggregate.
 * Defines persistence operations without exposing implementation details.
 */
public interface UserRepository {
    /**
     * Saves a user (create or update).
     */
    User save(User user);
    
    /**
     * Finds a user by ID.
     */
    Optional<User> findById(UUID id);
    
    /**
     * Finds a user by email.
     */
    Optional<User> findByEmail(Email email);
    
    /**
     * Finds a user by verification token.
     */
    Optional<User> findByVerificationToken(String token);
    
    /**
     * Checks if a user with the given email exists.
     */
    boolean existsByEmail(Email email);
    
    /**
     * Deletes a user.
     */
    void delete(User user);
    
    /**
     * Finds all active users.
     */
    Iterable<User> findAllActive();
}
