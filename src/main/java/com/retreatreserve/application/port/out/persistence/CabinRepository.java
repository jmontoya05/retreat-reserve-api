package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.CabinStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Cabin aggregate.
 * Cabin owns: CabinImage, CabinFeature, Policy, PolicyItem.
 */
public interface CabinRepository {

    /**
     * Saves a cabin (create or update).
     * Cascades to children: images, features, policies.
     */
    Cabin save(Cabin cabin);
    
    /**
     * Finds a cabin by ID.
     */
    Optional<Cabin> findById(UUID id);
    
    /**
     * Finds all active cabins.
     */
    List<Cabin> findAllActive();
    
    /**
     * Finds cabins by category.
     */
    List<Cabin> findByCategoryId(UUID categoryId);
    
    /**
     * Finds cabins by city.
     */
    List<Cabin> findByCity(String city);
    
    /**
     * Finds available cabins by filters.
     */
    List<Cabin> findAvailableCabins(
        String city,
        Integer maxGuests,
        BigDecimal maxPrice
    );
    
    /**
     * Finds cabins with specific feature.
     */
    List<Cabin> findByFeatureId(UUID featureId);
    
    /**
     * Finds cabins by status.
     */
    List<Cabin> findByStatus(CabinStatus status);
    
    /**
     * Deletes a cabin.
     */
    void delete(Cabin cabin);
}
