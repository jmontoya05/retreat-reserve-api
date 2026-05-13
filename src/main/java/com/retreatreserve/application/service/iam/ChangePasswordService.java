package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.ChangePasswordCommand;
import com.retreatreserve.application.port.in.iam.ChangePasswordUseCase;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.application.port.out.security.PasswordEncoder;
import com.retreatreserve.domain.exception.user.InvalidPasswordException;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.iam.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangePasswordService implements ChangePasswordUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void execute(ChangePasswordCommand command) {
        log.info("Changing password for user: {}", command.userId());
        
        User user = userRepository.findById(UUID.fromString(command.userId()))
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(command.currentPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        
        String newHash = passwordEncoder.encode(command.newPassword());
        user.updatePassword(newHash);
        
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", user.getId());
    }
}
