package com.retreatreserve.infrastructure.adapter.in.rest.favorite.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddFavoriteRequest(
    @NotBlank(message = "Cabin ID is required")
    String cabinId
) {}
