package com.retreatreserve.application.service.iam;

import com.retreatreserve.application.port.in.iam.GetUserByIdUseCase;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.iam.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByIdService implements GetUserByIdUseCase {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional(readOnly = true)
    public User execute(String userId) {
        return userRepository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
}
