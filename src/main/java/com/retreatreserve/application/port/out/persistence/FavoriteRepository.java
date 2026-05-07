package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.reservation.Favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Favorite aggregate.
 */
public interface FavoriteRepository {
    
    Favorite save(Favorite favorite);
    
    Optional<Favorite> findById(UUID id);
    
    List<Favorite> findByUserId(UUID userId);
    
    Optional<Favorite> findByUserIdAndCabinId(UUID userId, UUID cabinId);
    
    boolean existsByUserIdAndCabinId(UUID userId, UUID cabinId);
    
    void delete(Favorite favorite);
    
    void deleteByUserIdAndCabinId(UUID userId, UUID cabinId);
}
