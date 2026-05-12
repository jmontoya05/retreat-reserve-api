package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record UpdatePricingRequest(
    @NotNull(message = "New price per night is required")
    BigDecimal newPricePerNight
)
{}
