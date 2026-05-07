package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.model.cabin.Feature;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.FeatureJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.FeatureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FeatureRepositoryImpl implements FeatureRepository {
    
    private final FeatureJpaRepository jpaRepository;
    private final FeatureMapper mapper;
    
    @Override
    @Transactional
    public Feature save(Feature feature) {
        var jpaEntity = mapper.toJpaEntity(feature);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Feature> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Feature> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Feature> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Feature> findByIds(List<UUID> ids) {
        return jpaRepository.findByIdIn(ids).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
    
    @Override
    @Transactional
    public void delete(Feature feature) {
        jpaRepository.deleteById(feature.getId());
    }
}
