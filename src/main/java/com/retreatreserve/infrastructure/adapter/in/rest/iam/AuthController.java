package com.retreatreserve.infrastructure.adapter.in.rest.iam;

import com.retreatreserve.application.command.iam.RegisterUserCommand;
import com.retreatreserve.application.command.iam.VerifyEmailCommand;
import com.retreatreserve.application.port.in.iam.RegisterUserUseCase;
import com.retreatreserve.application.port.in.iam.VerifyEmailUseCase;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.RegisterRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.VerifyEmailRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response.UserResponse;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final RegisterUserUseCase registerUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
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
    
    @PostMapping("/verify-email")
    public ResponseEntity<UserResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        VerifyEmailCommand command = new VerifyEmailCommand(request.token());
        User user = verifyEmailUseCase.execute(command);
        return ResponseEntity.ok(userDtoMapper.toResponse(user));
    }
}
