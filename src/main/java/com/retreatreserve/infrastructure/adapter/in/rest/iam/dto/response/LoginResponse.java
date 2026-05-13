package com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response;

public record LoginResponse(
    String token,
    String userId,
    String email,
    String role
) {}
