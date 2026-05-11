package com.retreatreserve.application.port.in.iam;

import com.retreatreserve.application.command.iam.UpdateUserProfileCommand;
import com.retreatreserve.domain.model.iam.User;

public interface UpdateUserProfileUseCase {
    User execute(UpdateUserProfileCommand command);
}
