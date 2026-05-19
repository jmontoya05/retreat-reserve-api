package com.retreatreserve.application.port.in.iam;

import com.retreatreserve.application.dto.iam.LoginResult;

public interface LoginUseCase {
    
    LoginResult execute(String email, String password);
}
