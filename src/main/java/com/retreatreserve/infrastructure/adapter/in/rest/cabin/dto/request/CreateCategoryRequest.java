package com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
    @NotBlank(message = "Category name is required")
    String name,

    String description,
    
    String imageKey
) 
{}
