package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.VerifyEmailCommand;
import com.retreatreserve.application.port.in.iam.VerifyEmailUseCase;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.exception.user.InvalidVerificationTokenException;
import com.retreatreserve.domain.model.iam.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for email verification.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyEmailService implements VerifyEmailUseCase {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public User execute(VerifyEmailCommand command) {
        log.info("Verifying email with token: {}", command.token());
        
        User user = userRepository.findByVerificationToken(command.token())
            .orElseThrow(() -> new InvalidVerificationTokenException("Invalid verification token"));
        
        user.verifyEmail(command.token());
        
        User verified = userRepository.save(user);
        
        log.info("Email verified successfully for user: {}", verified.getId());
        return verified;
    }
}
