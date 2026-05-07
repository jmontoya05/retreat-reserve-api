package com.retreatreserve.infrastructure.adapter.out.persistence.jpa;

import com.retreatreserve.domain.model.reservation.ReservationStatus;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID> {
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.user.id = :userId AND r.active = true")
    List<ReservationJpaEntity> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.cabin.id = :cabinId AND r.active = true")
    List<ReservationJpaEntity> findByCabinId(@Param("cabinId") UUID cabinId);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.user.id = :userId AND r.status = :status AND r.active = true")
    List<ReservationJpaEntity> findByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") ReservationStatus status);
    
    /**
     * Find active reservations that overlap with given date range for a specific cabin.
     * Critical for availability checking!
     */
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "r.cabin.id = :cabinId AND r.active = true " +
           "AND r.status IN ('CONFIRMED', 'PENDING') " +
           "AND r.dateRange.checkOutDate > :checkInDate " +
           "AND r.dateRange.checkInDate < :checkOutDate")
    List<ReservationJpaEntity> findOverlappingReservations(
        @Param("cabinId") UUID cabinId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate
    );
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "r.status = 'CONFIRMED' AND r.active = true " +
           "AND r.dateRange.checkOutDate < :currentDate")
    List<ReservationJpaEntity> findReservationsToComplete(@Param("currentDate") LocalDate currentDate);
}
