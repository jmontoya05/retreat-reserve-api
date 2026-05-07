package com.retreatreserve.infrastructure.adapter.out.persistence.jpa;

import com.retreatreserve.infrastructure.adapter.out.persistence.entity.FavoriteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteJpaEntity, UUID> {
    
    @Query("SELECT f FROM FavoriteJpaEntity f WHERE f.user.id = :userId")
    List<FavoriteJpaEntity> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT f FROM FavoriteJpaEntity f WHERE f.user.id = :userId AND f.cabin.id = :cabinId")
    Optional<FavoriteJpaEntity> findByUserIdAndCabinId(@Param("userId") UUID userId, @Param("cabinId") UUID cabinId);
    
    boolean existsByUserIdAndCabinId(UUID userId, UUID cabinId);
    
    @Modifying
    @Query("DELETE FROM FavoriteJpaEntity f WHERE f.user.id = :userId AND f.cabin.id = :cabinId")
    void deleteByUserIdAndCabinId(@Param("userId") UUID userId, @Param("cabinId") UUID cabinId);
}
