package com.retreatreserve.application.command.iam;

public record VerifyEmailCommand(
    String token
) {}
