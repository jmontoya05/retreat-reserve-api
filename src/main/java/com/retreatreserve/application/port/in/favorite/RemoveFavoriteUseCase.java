package com.retreatreserve.application.port.in.favorite;

public interface RemoveFavoriteUseCase {
    void execute(String userId, String cabinId);
}
