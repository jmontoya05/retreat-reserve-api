package com.retreatreserve.infrastructure.adapter.out.persistence.jpa;

import com.retreatreserve.infrastructure.adapter.out.persistence.entity.FeatureJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeatureJpaRepository extends JpaRepository<FeatureJpaEntity, UUID> {
    
    Optional<FeatureJpaEntity> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT f FROM FeatureJpaEntity f WHERE f.active = true")
    List<FeatureJpaEntity> findAllActive();
    
    @Query("SELECT f FROM FeatureJpaEntity f WHERE f.id IN :ids")
    List<FeatureJpaEntity> findByIdIn(@Param("ids") List<UUID> ids);
}
