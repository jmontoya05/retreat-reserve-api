package com.retreatreserve.domain.model.iam;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.retreatreserve.domain.exception.user.ExpiredVerificationTokenException;
import com.retreatreserve.domain.exception.user.InvalidVerificationTokenException;

import lombok.Getter;

/**
 * User entity - Aggregate Root for Identity and Access Management.
 */
@Getter
public class User {
    private UUID id;
    private FullName fullName;
    private Email email;
    private String passwordHash;
    private String phoneNumber;
    private Role role;
    private Boolean emailVerified;
    private String verificationToken;
    private LocalDateTime verificationTokenExpiry;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== Constructors ==========

    protected User() {
    }

    /**
     * Creates a new User.
     */
    public User(FullName fullName, Email email, String passwordHash, String phoneNumber) {
        this.fullName = Objects.requireNonNull(fullName, "Full name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash cannot be null");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        this.role = Role.USER;
        this.emailVerified = false;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Reconstitutes a User from persistence (for repositories).
     */
    public User(UUID id, FullName fullName, Email email, String passwordHash, String phoneNumber,
                Role role, Boolean emailVerified, String verificationToken,
                LocalDateTime verificationTokenExpiry, Boolean active,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.emailVerified = emailVerified;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiry = verificationTokenExpiry;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Domain Methods ==========

    /**
     * Verifies the user's email using the verification token.
     */
    public void verifyEmail(String token) {
        if (this.verificationToken == null || !this.verificationToken.equals(token)) {
            throw new InvalidVerificationTokenException("Invalid verification token");
        }

        if (LocalDateTime.now().isAfter(verificationTokenExpiry)) {
            throw new ExpiredVerificationTokenException("Verification token has expired");
        }

        this.emailVerified = true;
        this.verificationToken = null;
        this.verificationTokenExpiry = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the verification token for email confirmation.
     */
    public void setVerificationToken(String token, int expiryHours) {
        this.verificationToken = token;
        this.verificationTokenExpiry = LocalDateTime.now().plusHours(expiryHours);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Promotes this user to admin role.
     */
    public void promoteToAdmin() {
        this.role = Role.ADMIN;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Demotes this user to regular user role.
     */
    public void demoteToUser() {
        this.role = Role.USER;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates this user account (soft delete).
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reactivates this user account.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if this user is an administrator.
     */
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    /**
     * Updates the user's password.
     */
    public void updatePassword(String newPasswordHash) {
        this.passwordHash = Objects.requireNonNull(newPasswordHash, "Password hash cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the user's profile information.
     */
    public void updateProfile(FullName fullName, String phoneNumber) {
        this.fullName = Objects.requireNonNull(fullName, "Full name cannot be null");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        this.updatedAt = LocalDateTime.now();
    }

    // ========== Equals & HashCode ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
