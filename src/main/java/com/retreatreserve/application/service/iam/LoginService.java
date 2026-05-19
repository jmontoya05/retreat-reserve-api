package com.retreatreserve.application.service.iam;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retreatreserve.application.dto.iam.LoginResult;
import com.retreatreserve.application.port.in.iam.LoginUseCase;
import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.application.port.out.security.AuthenticationService;
import com.retreatreserve.application.port.out.security.TokenProvider;
import com.retreatreserve.domain.exception.user.InvalidCredentialsException;
import com.retreatreserve.domain.exception.user.UserNotFoundException;
import com.retreatreserve.domain.model.iam.Email;
import com.retreatreserve.domain.model.iam.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService implements LoginUseCase {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationManager;
    private final TokenProvider jwtTokenProvider;

    @Override
    @Transactional(readOnly = true)
    public LoginResult execute(String email, String password) {
        log.info("Login attempt for email: {}", email);
        
        User user = userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        String userId = user.getId().toString();
        
        try {
            authenticationManager.authenticate(
                userId,
                password
            );
            
            String token = jwtTokenProvider.generateToken(userId);
            
            log.info("Login successful for user: {}", user.getId());
            return new LoginResult(token, user);
            
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {}", email);
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }    
}
