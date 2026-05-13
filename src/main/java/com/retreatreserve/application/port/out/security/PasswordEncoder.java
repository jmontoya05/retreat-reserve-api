package com.retreatreserve.application.port.out.security;

public interface PasswordEncoder {
    
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
