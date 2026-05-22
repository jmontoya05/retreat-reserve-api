package com.retreatreserve.application.service.favorite;

import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import com.retreatreserve.domain.model.reservation.Favorite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserFavoritesServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private GetUserFavoritesService getUserFavoritesService;

    @Test
    void shouldReturnFavoritesForUser() {
        UUID userId = UUID.randomUUID();
        Favorite favorite = new Favorite(UUID.randomUUID(), userId, UUID.randomUUID(), LocalDateTime.now());

        when(favoriteRepository.findByUserId(userId)).thenReturn(List.of(favorite));

        List<Favorite> result = getUserFavoritesService.execute(userId.toString());

        assertEquals(1, result.size());
        assertEquals(favorite, result.get(0));
    }
}
