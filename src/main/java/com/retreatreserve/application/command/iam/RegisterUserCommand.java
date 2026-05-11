package com.retreatreserve.application.command.iam;

public record RegisterUserCommand(
    String firstName,
    String lastName,
    String email,
    String password,
    String phoneNumber
) {
    public RegisterUserCommand {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
    }
}
