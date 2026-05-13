package com.retreatreserve.infrastructure.adapter.out.security.userdetails;

import com.retreatreserve.application.port.out.persistence.UserRepository;
import com.retreatreserve.domain.model.iam.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
        
        return UserPrincipal.create(user);
    }    
}
