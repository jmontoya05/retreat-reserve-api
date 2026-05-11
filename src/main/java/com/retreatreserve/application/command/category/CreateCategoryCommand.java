package com.retreatreserve.application.command.category;

public record CreateCategoryCommand(
    String name,
    String description,
    String imageUrl
) {}
