package com.retreatreserve.infrastructure.adapter.in.rest.mapper;

import com.retreatreserve.domain.model.iam.User;
import com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response.UserResponse;

import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    
    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId().toString(),
            user.getFullName().getFirstName(),
            user.getFullName().getLastName(),
            user.getEmail().getValue(),
            user.getPhoneNumber(),
            user.getRole().name(),
            user.getEmailVerified()
        );
    }
}
