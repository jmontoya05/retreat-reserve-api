package com.retreatreserve.infrastructure.adapter.out.persistence.jpa;

import com.retreatreserve.domain.model.cabin.CabinStatus;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.CabinJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CabinJpaRepository extends JpaRepository<CabinJpaEntity, UUID> {
    
    @Query("SELECT c FROM CabinJpaEntity c WHERE c.active = true")
    List<CabinJpaEntity> findAllActive();
    
    @Query("SELECT c FROM CabinJpaEntity c WHERE c.category.id = :categoryId AND c.active = true")
    List<CabinJpaEntity> findByCategoryId(@Param("categoryId") UUID categoryId);
    
    @Query("SELECT c FROM CabinJpaEntity c WHERE c.location.city = :city AND c.active = true")
    List<CabinJpaEntity> findByCity(@Param("city") String city);
    
    @Query("SELECT c FROM CabinJpaEntity c WHERE " +
           "c.active = true AND c.status = 'AVAILABLE' " +
           "AND (:city IS NULL OR c.location.city = :city) " +
           "AND (:maxGuests IS NULL OR c.maxGuests >= :maxGuests) " +
           "AND (:maxPrice IS NULL OR c.pricePerNight <= :maxPrice)")
    List<CabinJpaEntity> findAvailableCabins(
        @Param("city") String city,
        @Param("maxGuests") Integer maxGuests,
        @Param("maxPrice") BigDecimal maxPrice
    );
    
    @Query("SELECT DISTINCT c FROM CabinJpaEntity c " +
           "JOIN c.cabinFeatures cf " +
           "WHERE cf.feature.id = :featureId AND c.active = true")
    List<CabinJpaEntity> findByFeatureId(@Param("featureId") UUID featureId);
    
    @Query("SELECT c FROM CabinJpaEntity c WHERE c.status = :status AND c.active = true")
    List<CabinJpaEntity> findByStatus(@Param("status") CabinStatus status);
}
