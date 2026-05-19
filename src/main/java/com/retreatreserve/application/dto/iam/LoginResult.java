package com.retreatreserve.application.dto.iam;

import com.retreatreserve.domain.model.iam.User;

public record LoginResult(String token, User user) {}
