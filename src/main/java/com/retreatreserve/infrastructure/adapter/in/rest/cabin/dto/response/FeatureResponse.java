package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response;

public record FeatureResponse(
    String id,
    String name,
    String iconUrl,
    String description
) {}
