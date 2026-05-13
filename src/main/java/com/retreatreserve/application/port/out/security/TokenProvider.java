package com.retreatreserve.application.port.out.security;

public interface TokenProvider {

    String generateToken(String userId);

    String getUserIdFromToken(String token);

    boolean validateToken(String token);
}
