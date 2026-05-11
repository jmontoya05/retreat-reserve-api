package com.retreatreserve.application.command.iam;

public record ChangePasswordCommand(
    String userId,
    String currentPassword,
    String newPassword
) {}
