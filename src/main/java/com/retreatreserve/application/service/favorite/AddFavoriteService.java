package com.retreatreserve.application.service.favorite;

import com.retreatreserve.application.command.favorite.AddFavoriteCommand;
import com.retreatreserve.application.port.in.favorite.AddFavoriteUseCase;
import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import com.retreatreserve.domain.exception.reservation.FavoriteAlreadyExistsException;
import com.retreatreserve.domain.model.reservation.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddFavoriteService implements AddFavoriteUseCase {
    
    private final FavoriteRepository favoriteRepository;
    
    @Override
    @Transactional
    public Favorite execute(AddFavoriteCommand command) {
        UUID userId = UUID.fromString(command.userId());
        UUID cabinId = UUID.fromString(command.cabinId());
        
        if (favoriteRepository.existsByUserIdAndCabinId(userId, cabinId)) {
            throw new FavoriteAlreadyExistsException("Cabin already in favorites");
        }
        
        Favorite favorite = new Favorite(userId, cabinId);
        return favoriteRepository.save(favorite);
    }
}
