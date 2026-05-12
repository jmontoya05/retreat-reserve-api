package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response;

public record CategoryResponse(
    String id,
    String name,
    String description,
    String imageUrl
) {}
