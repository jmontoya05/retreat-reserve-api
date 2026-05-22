package com.retreatreserve.application.service.favorite;

import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveFavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private RemoveFavoriteService removeFavoriteService;

    @Test
    void shouldDeleteFavoriteByUserAndCabinId() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();

        removeFavoriteService.execute(userId.toString(), cabinId.toString());

        verify(favoriteRepository).deleteByUserIdAndCabinId(userId, cabinId);
    }
}
