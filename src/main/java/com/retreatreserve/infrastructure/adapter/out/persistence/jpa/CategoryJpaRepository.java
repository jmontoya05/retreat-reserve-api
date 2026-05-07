package com.retreatreserve.infrastructure.adapter.out.persistence.jpa;

import com.retreatreserve.infrastructure.adapter.out.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID> {
    
    Optional<CategoryJpaEntity> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT c FROM CategoryJpaEntity c WHERE c.active = true")
    List<CategoryJpaEntity> findAllActive();
}
