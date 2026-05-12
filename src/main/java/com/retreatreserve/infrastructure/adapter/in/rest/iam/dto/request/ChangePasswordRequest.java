package com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest (
    @NotBlank(message = "Current is required")
    String currentPassword,

    @NotBlank(message = "New password is required")
    String newPassword
)
{}
