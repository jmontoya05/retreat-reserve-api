package com.retreatreserve.application.port.in.iam;

import com.retreatreserve.domain.model.iam.User;

public interface GetUserByIdUseCase {
    User execute(String userId);
}
