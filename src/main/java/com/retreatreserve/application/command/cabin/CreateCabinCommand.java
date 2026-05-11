package com.retreatreserve.application.command.cabin;

import java.math.BigDecimal;
import java.util.List;

public record CreateCabinCommand(
    String name,
    String description,
    String categoryId,
    String city,
    String state,
    String country,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    Integer maxGuests,
    Integer numberOfBedrooms,
    Integer numberOfBathrooms,
    BigDecimal pricePerNight,
    List<String> imageUrls,
    List<String> featureIds,
    List<PolicyInput> policies
) {
    public record PolicyInput(
        String title,
        Integer displayOrder,
        List<String> items
    ) {}
}
