package com.retreatreserve.application.port.out.security;

public interface AuthenticationService {

    void authenticate(String userId, String password);
}
