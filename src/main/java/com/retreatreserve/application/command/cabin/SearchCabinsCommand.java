package com.retreatreserve.application.command.cabin;

import java.math.BigDecimal;

public record SearchCabinsCommand(
    String city,
    Integer maxGuests,
    BigDecimal maxPrice,
    String categoryId
) {}
