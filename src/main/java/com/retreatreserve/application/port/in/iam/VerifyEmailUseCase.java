package com.retreatreserve.application.port.in.iam;

import com.retreatreserve.application.command.iam.VerifyEmailCommand;
import com.retreatreserve.domain.model.iam.User;

public interface VerifyEmailUseCase {
    User execute(VerifyEmailCommand command);
}
