package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateCabinRequest(
    @NotBlank(message = "Cabin name is required")
    String name,

    @NotBlank(message = "Cabin description is required")
    String description,

    @NotBlank(message = "Category ID is required")
    String categoryId,

    @NotBlank(message = "City is required")
    String city,

    @NotBlank(message = "State is required")
    String state,

    @NotBlank(message = "Country is required")
    String country,

    String address,

    BigDecimal latitude,

    BigDecimal longitude,

    @NotNull(message = "Maximum guests is required")
    @Min(value = 1, message = "Maximum guests must be at least 1")
    Integer maxGuests,

    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 1, message = "Number of bedrooms must be at least 1")
    Integer numberOfBedrooms,

    @NotNull(message = "Number of bathrooms is required")
    @Min(value = 1, message = "Number of bathrooms must be at least 1")
    Integer numberOfBathrooms,

    @NotNull(message = "Price per night is required")
    BigDecimal pricePerNight,

    List<String> imageUrls,
   
    List<String> featureIds,
    
    List<PolicyRequest> policies
) {
    public record PolicyRequest(String title, Integer displayOrder, List<String> items) {}
}
