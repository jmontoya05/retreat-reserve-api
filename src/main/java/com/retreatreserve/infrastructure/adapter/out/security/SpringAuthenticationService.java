package com.retreatreserve.infrastructure.adapter.out.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.retreatreserve.application.port.out.security.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String userId, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userId, password)
        );
    }
}
