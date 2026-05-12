package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateFeatureRequest (
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Icon URL is required")
    String iconUrl,

    String description
) 
{}
