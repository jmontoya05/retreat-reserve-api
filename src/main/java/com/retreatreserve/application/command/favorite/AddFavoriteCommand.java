package com.retreatreserve.application.command.favorite;

public record AddFavoriteCommand(
    String userId,
    String cabinId
) {}
