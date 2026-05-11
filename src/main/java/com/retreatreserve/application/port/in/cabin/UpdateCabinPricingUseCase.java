package com.retreatreserve.application.port.in.cabin;

import java.math.BigDecimal;

import com.retreatreserve.domain.model.cabin.Cabin;

public interface UpdateCabinPricingUseCase {
    Cabin execute(String cabinId, BigDecimal newPrice);
}
