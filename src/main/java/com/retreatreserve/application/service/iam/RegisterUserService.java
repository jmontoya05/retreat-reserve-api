package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.RegisterUserCommand;
import com.retreatreserve.application.port.in.iam.RegisterUserUseCase;
import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.application.port.out.security.PasswordEncoder;
import com.retreatreserve.domain.exception.user.EmailAlreadyExistsException;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.exception.notification.EmailSendingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case for user registration.
 * Orchestrates: validation, password hashing, persistence, email verification.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public User execute(RegisterUserCommand command) {
        log.info("Registering new user with email: {}", command.email());

        Email email = new Email(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already registered: " + command.email());
        }

        FullName fullName = new FullName(command.firstName(), command.lastName());
        String passwordHash = passwordEncoder.encode(command.password());

        User user = new User(fullName, email, passwordHash, command.phoneNumber());

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken, 24); // 24 hours expiry

        User savedUser = userRepository.save(user);

        try {
            emailService.sendVerificationEmail(
                    savedUser.getEmail().getValue(),
                    savedUser.getFullName().getFullName(),
                    verificationToken);
            log.info("Sending verification email to: {}", savedUser.getEmail().getValue());
        } catch (Exception e) {
            log.error("Failed to send verification email", e);
            throw new EmailSendingException(
                    "Failed to send verification email",
                    e);
        }

        log.info("User registered successfully: {}", savedUser.getId());
        return savedUser;
    }
}
