package com.retreatreserve.application.port.in.favorite;

import java.util.List;

import com.retreatreserve.domain.model.reservation.Favorite;

public interface GetUserFavoritesUseCase {
    List<Favorite> execute(String userId);
}
