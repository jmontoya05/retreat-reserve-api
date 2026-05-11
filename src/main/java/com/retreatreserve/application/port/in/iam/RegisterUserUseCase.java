package com.retreatreserve.application.port.in.iam;

import com.retreatreserve.application.command.iam.RegisterUserCommand;
import com.retreatreserve.domain.model.iam.User;

public interface RegisterUserUseCase {
    User execute(RegisterUserCommand command);
}
