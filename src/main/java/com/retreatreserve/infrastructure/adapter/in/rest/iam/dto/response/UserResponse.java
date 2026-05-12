package com.retreatreserve.infrastructure.adapter.in.rest.iam.dto.response;

public record UserResponse(
    String id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String role,
    Boolean emailVerified
) {}
