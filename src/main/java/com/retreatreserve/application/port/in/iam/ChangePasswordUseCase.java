package com.retreatreserve.application.port.in.iam;

import com.retreatreserve.application.command.iam.ChangePasswordCommand;

public interface ChangePasswordUseCase {
    void execute(ChangePasswordCommand command);
}
