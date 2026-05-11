package com.retreatreserve.application.port.in.favorite;

import com.retreatreserve.application.command.favorite.AddFavoriteCommand;
import com.retreatreserve.domain.model.reservation.Favorite;

public interface AddFavoriteUseCase {
    Favorite execute(AddFavoriteCommand command);
}
