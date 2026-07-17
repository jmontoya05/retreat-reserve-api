package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CabinResponse(
    String id,
    String name,
    String description,
    String categoryId,
    LocationResponse location,
    Integer maxGuests,
    Integer numberOfBedrooms,
    Integer numberOfBathrooms,
    BigDecimal pricePerNight,
    BigDecimal averageRating,
    Integer totalReviews,
    String status,
    List<String> imageUrls,
    List<String> featureIds,
    List<PolicyResponse> policies
) {
    public record LocationResponse(String city, String state, String country) {}
    public record PolicyResponse(String id, String title, Integer displayOrder, List<PolicyItemResponse> items) {}
    public record PolicyItemResponse(String id, String description, Integer displayOrder) {}
}
