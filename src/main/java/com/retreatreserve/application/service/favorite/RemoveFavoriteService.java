package com.retreatreserve.application.service.favorite;

import com.retreatreserve.application.port.in.favorite.RemoveFavoriteUseCase;
import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveFavoriteService implements RemoveFavoriteUseCase {
    
    private final FavoriteRepository favoriteRepository;
    
    @Override
    @Transactional
    public void execute(String userId, String cabinId) {
        favoriteRepository.deleteByUserIdAndCabinId(
            UUID.fromString(userId),
            UUID.fromString(cabinId)
        );
    }
}
