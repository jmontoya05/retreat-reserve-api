package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.command.iam.UpdateUserProfileCommand;
import com.retreatreserve.application.port.in.iam.UpdateUserProfileUseCase;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.iam.FullName;
import com.retreatreserve.domain.model.iam.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserProfileService implements UpdateUserProfileUseCase {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public User execute(UpdateUserProfileCommand command) {
        log.info("Updating profile for user: {}", command.userId());
        
        User user = userRepository.findById(UUID.fromString(command.userId()))
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        FullName fullName = new FullName(command.firstName(), command.lastName());
        user.updateProfile(fullName, command.phoneNumber());
        
        return userRepository.save(user);
    }
}
