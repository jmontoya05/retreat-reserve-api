package com.retreatreserve.infrastructure.adapter.out.persistence.jpa;

import com.retreatreserve.infrastructure.adapter.out.persistence.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, UUID> {
    
    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.cabin.id = :cabinId AND r.active = true")
    List<ReviewJpaEntity> findByCabinId(@Param("cabinId") UUID cabinId);
    
    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.user.id = :userId AND r.active = true")
    List<ReviewJpaEntity> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.reservation.id = :reservationId")
    Optional<ReviewJpaEntity> findByReservationId(@Param("reservationId") UUID reservationId);
    
    boolean existsByReservationId(UUID reservationId);
    
    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.cabin.id = :cabinId AND r.active = true ORDER BY r.createdAt DESC")
    List<ReviewJpaEntity> findActiveByCabinIdOrderByCreatedAtDesc(@Param("cabinId") UUID cabinId);
}
