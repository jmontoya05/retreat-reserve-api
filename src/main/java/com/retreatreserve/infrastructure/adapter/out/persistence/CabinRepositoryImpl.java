package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.CabinStatus;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.CategoryJpaEntity;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.FeatureJpaEntity;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.CabinJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.CategoryJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.FeatureJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.CabinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of CabinRepository.
 * Handles cabin aggregate with its children (images, features, policies).
 */
@Repository
@RequiredArgsConstructor
public class CabinRepositoryImpl implements CabinRepository {
    
    private final CabinJpaRepository jpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final FeatureJpaRepository featureJpaRepository;
    private final CabinMapper mapper;
    
    @Override
    @Transactional
    public Cabin save(Cabin cabin) {
        // Fetch required related entities
        CategoryJpaEntity categoryJpa = categoryJpaRepository.findById(cabin.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + cabin.getCategoryId()));
        
        List<FeatureJpaEntity> featureJpaEntities = featureJpaRepository.findByIdIn(cabin.getFeatureIds());
        
        // Convert to JPA entity (cascades to children)
        var jpaEntity = mapper.toJpaEntity(cabin, categoryJpa, featureJpaEntities);
        
        // Save (cascades to images, features, policies)
        var saved = jpaRepository.save(jpaEntity);
        
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Cabin> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> findByCategoryId(UUID categoryId) {
        return jpaRepository.findByCategoryId(categoryId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> findByCity(String city) {
        return jpaRepository.findByCity(city).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> findAvailableCabins(String city, Integer maxGuests, BigDecimal maxPrice) {
        return jpaRepository.findAvailableCabins(city, maxGuests, maxPrice).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> findByFeatureId(UUID featureId) {
        return jpaRepository.findByFeatureId(featureId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> findByStatus(CabinStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional
    public void delete(Cabin cabin) {
        jpaRepository.deleteById(cabin.getId());
    }
}
