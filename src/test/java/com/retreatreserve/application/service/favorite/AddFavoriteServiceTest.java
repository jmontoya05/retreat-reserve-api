package com.retreatreserve.application.service.favorite;

import com.retreatreserve.application.command.favorite.AddFavoriteCommand;
import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import com.retreatreserve.domain.exception.reservation.FavoriteAlreadyExistsException;
import com.retreatreserve.domain.model.reservation.Favorite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddFavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private AddFavoriteService addFavoriteService;

    @Test
    void shouldAddFavoriteWhenNotAlreadyPresent() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        AddFavoriteCommand command = new AddFavoriteCommand(userId.toString(), cabinId.toString());
        Favorite favorite = new Favorite(userId, cabinId);

        when(favoriteRepository.existsByUserIdAndCabinId(userId, cabinId)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        Favorite result = addFavoriteService.execute(command);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(cabinId, result.getCabinId());
    }

    @Test
    void shouldThrowWhenFavoriteAlreadyExists() {
        UUID userId = UUID.randomUUID();
        UUID cabinId = UUID.randomUUID();
        AddFavoriteCommand command = new AddFavoriteCommand(userId.toString(), cabinId.toString());

        when(favoriteRepository.existsByUserIdAndCabinId(userId, cabinId)).thenReturn(true);

        assertThrows(FavoriteAlreadyExistsException.class, () -> addFavoriteService.execute(command));
    }
}
