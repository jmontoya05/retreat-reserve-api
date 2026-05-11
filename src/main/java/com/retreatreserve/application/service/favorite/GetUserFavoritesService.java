package com.retreatreserve.application.service.favorite;

import com.retreatreserve.application.port.in.favorite.GetUserFavoritesUseCase;
import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import com.retreatreserve.domain.model.reservation.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserFavoritesService implements GetUserFavoritesUseCase {
    
    private final FavoriteRepository favoriteRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Favorite> execute(String userId) {
        return favoriteRepository.findByUserId(UUID.fromString(userId));
    }
}
