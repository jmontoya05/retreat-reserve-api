package com.retreatreserve.application.command.feature;

public record CreateFeatureCommand(
    String name,
    String iconKey,
    String description
) {}
