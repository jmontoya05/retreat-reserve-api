package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.cabin.Feature;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Feature aggregate.
 */
public interface FeatureRepository {
    
    Feature save(Feature feature);
    
    Optional<Feature> findById(UUID id);
    
    Optional<Feature> findByName(String name);
    
    List<Feature> findAllActive();
    
    List<Feature> findByIds(List<UUID> ids);
    
    boolean existsByName(String name);
    
    void delete(Feature feature);
}