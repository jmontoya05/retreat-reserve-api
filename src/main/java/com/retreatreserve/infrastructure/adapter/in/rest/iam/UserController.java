package com.retreatreserve.infrastructure.adapter.in.rest.iam;

import com.retreatreserve.application.command.iam.ChangePasswordCommand;
import com.retreatreserve.application.command.iam.UpdateUserProfileCommand;
import com.retreatreserve.application.port.in.iam.ChangePasswordUseCase;
import com.retreatreserve.application.port.in.iam.GetUserByIdUseCase;
import com.retreatreserve.application.port.in.iam.UpdateUserProfileUseCase;
import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.ChangePasswordRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request.UpdateProfileRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response.UserResponse;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.UserDtoMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final UserDtoMapper userDtoMapper;
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        User user = getUserByIdUseCase.execute(userId);
        return ResponseEntity.ok(userDtoMapper.toResponse(user));
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable String userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
            userId, request.firstName(), request.lastName(), request.phoneNumber()
        );
        User user = updateUserProfileUseCase.execute(command);
        return ResponseEntity.ok(userDtoMapper.toResponse(user));
    }
    
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        
        ChangePasswordCommand command = new ChangePasswordCommand(
            userId, request.currentPassword(), request.newPassword()
        );
        changePasswordUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }
}
