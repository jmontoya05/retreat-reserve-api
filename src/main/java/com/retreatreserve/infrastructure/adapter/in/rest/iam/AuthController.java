package com.retreatreserve.infrastructure.adapter.in.rest.iam;

import com.retreatreserve.application.command.iam.RegisterUserCommand;
import com.retreatreserve.application.command.iam.VerifyEmailCommand;
import com.retreatreserve.application.dto.iam.LoginResult;
import com.retreatreserve.application.port.in.iam.LoginUseCase;
import com.retreatreserve.application.port.in.iam.RegisterUserUseCase;
import com.retreatreserve.application.port.in.iam.VerifyEmailUseCase;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.LoginRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.RegisterRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.VerifyEmailRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response.LoginResponse;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response.UserResponse;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.UserDtoMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and email verification")
public class AuthController {
    
    private final RegisterUserUseCase registerUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final LoginUseCase loginUseCase;
    private final UserDtoMapper userDtoMapper;
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.password(),
            request.phoneNumber()
        );
        
        User user = registerUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoMapper.toResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = loginUseCase.execute(request.email(), request.password());
        
        LoginResponse response = new LoginResponse(
            result.token(),
            result.user().getId().toString(),
            result.user().getEmail().getValue(),
            result.user().getRole().name()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify-email")
    public ResponseEntity<UserResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        VerifyEmailCommand command = new VerifyEmailCommand(request.token());
        User user = verifyEmailUseCase.execute(command);
        return ResponseEntity.ok(userDtoMapper.toResponse(user));
    }
}
