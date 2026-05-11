package com.retreatreserve.application.port.in.cabin;

import com.retreatreserve.domain.model.cabin.Cabin;

public interface GetCabinDetailsUseCase {
    Cabin execute(String cabinId);
}
