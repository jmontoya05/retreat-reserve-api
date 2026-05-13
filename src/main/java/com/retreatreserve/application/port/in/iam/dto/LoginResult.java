package com.retreatreserve.application.port.in.iam.dto;

import com.retreatreserve.domain.model.iam.User;

public record LoginResult(String token, User user) {}
