package com.retreatreserve.application.command.iam;

public record UpdateUserProfileCommand(
    String userId,
    String firstName,
    String lastName,
    String phoneNumber
) {}
