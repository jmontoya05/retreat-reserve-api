package com.retreatreserve.infrastructure.adapter.out.security;

import org.springframework.stereotype.Component;

import com.retreatreserve.application.port.out.security.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {
    
    private final org.springframework.security.crypto.password.PasswordEncoder delegate;

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
