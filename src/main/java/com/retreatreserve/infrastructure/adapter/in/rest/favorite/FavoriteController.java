package com.retreatreserve.infrastructure.adapter.in.rest.favorite;

import com.retreatreserve.application.command.favorite.AddFavoriteCommand;
import com.retreatreserve.application.port.in.favorite.AddFavoriteUseCase;
import com.retreatreserve.application.port.in.favorite.GetUserFavoritesUseCase;
import com.retreatreserve.application.port.in.favorite.RemoveFavoriteUseCase;
import com.retreatreserve.domain.model.reservation.Favorite;
import com.retreatreserve.infrastructure.adapter.in.rest.favorite.dto.request.AddFavoriteRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite Management", description = "Endpoints for adding, removing, and retrieving user favorites")
public class FavoriteController {
    
    private final AddFavoriteUseCase addFavoriteUseCase;
    private final RemoveFavoriteUseCase removeFavoriteUseCase;
    private final GetUserFavoritesUseCase getUserFavoritesUseCase;
    
    @PostMapping
    public ResponseEntity<String> addFavorite(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody AddFavoriteRequest request) {
        
        AddFavoriteCommand command = new AddFavoriteCommand(userId, request.cabinId());
        Favorite favorite = addFavoriteUseCase.execute(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite.getId().toString());
    }
    
    @DeleteMapping("/cabin/{cabinId}")
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String cabinId) {
        
        removeFavoriteUseCase.execute(userId, cabinId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getUserFavorites(@PathVariable String userId) {
        List<Favorite> favorites = getUserFavoritesUseCase.execute(userId);
        
        return ResponseEntity.ok(
            favorites.stream()
                .map(f -> f.getCabinId().toString())
                .toList()
        );
    }
}
