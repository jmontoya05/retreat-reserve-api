package com.retreatreserve.application.port.in.cabin;

import com.retreatreserve.application.command.cabin.CreateCabinCommand;
import com.retreatreserve.domain.model.cabin.Cabin;

public interface CreateCabinUseCase {
    Cabin execute(CreateCabinCommand command);
}
