package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.cabin.*;
import com.retreatreserve.infrastructure.adapter.out.persistence.embeddable.LocationEmbeddable;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Cabin aggregate.
 * Handles conversion of cabin with all its children: images, features, policies.
 */
@Component
@RequiredArgsConstructor
public class CabinMapper {
        
    public Cabin toDomain(CabinJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        // Map location
        Location location = mapLocationToDomain(jpaEntity.getLocation());
        
        // Map capacity
        Capacity capacity = new Capacity(jpaEntity.getMaxGuests());
        
        // Map images
        List<CabinImage> images = jpaEntity.getImages().stream()
            .map(this::mapImageToDomain)
            .toList();
        
        // Map feature IDs (domain only stores IDs)
        List<java.util.UUID> featureIds = jpaEntity.getCabinFeatures().stream()
            .map(cf -> cf.getFeature().getId())
            .toList();
        
        // Map policies
        List<Policy> policies = jpaEntity.getPolicies().stream()
            .map(this::mapPolicyToDomain)
            .toList();
        
        return new Cabin(
            jpaEntity.getId(),
            jpaEntity.getName(),
            jpaEntity.getDescription(),
            jpaEntity.getCategory().getId(),
            location,
            capacity,
            jpaEntity.getNumberOfBedrooms(),
            jpaEntity.getNumberOfBathrooms(),
            jpaEntity.getPricePerNight(),
            jpaEntity.getAverageRating(),
            jpaEntity.getTotalReviews(),
            jpaEntity.getStatus(),
            jpaEntity.getActive(),
            images,
            featureIds,
            policies,
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    public CabinJpaEntity toJpaEntity(Cabin domain, CategoryJpaEntity categoryJpa, 
                                      List<FeatureJpaEntity> featureJpaEntities) {
        if (domain == null) return null;
        
        CabinJpaEntity jpaEntity = new CabinJpaEntity();
        jpaEntity.setId(domain.getId());
        jpaEntity.setName(domain.getName());
        jpaEntity.setDescription(domain.getDescription());
        jpaEntity.setCategory(categoryJpa);
        jpaEntity.setLocation(mapLocationToEmbeddable(domain.getLocation()));
        jpaEntity.setMaxGuests(domain.getCapacity().getMaxGuests());
        jpaEntity.setNumberOfBedrooms(domain.getNumberOfBedrooms());
        jpaEntity.setNumberOfBathrooms(domain.getNumberOfBathrooms());
        jpaEntity.setPricePerNight(domain.getPricePerNight());
        jpaEntity.setAverageRating(domain.getAverageRating());
        jpaEntity.setTotalReviews(domain.getTotalReviews());
        jpaEntity.setStatus(domain.getStatus());
        jpaEntity.setActive(domain.getActive());
        jpaEntity.setCreatedAt(domain.getCreatedAt());
        jpaEntity.setUpdatedAt(domain.getUpdatedAt());
        
        // Map images
        List<CabinImageJpaEntity> imageEntities = domain.getImages().stream()
            .map(img -> mapImageToJpaEntity(img, jpaEntity))
            .toList();
        jpaEntity.setImages(imageEntities);
        
        // Map cabin-feature associations
        List<CabinFeatureJpaEntity> cabinFeatures = new ArrayList<>();
        for (int i = 0; i < domain.getFeatureIds().size(); i++) {
            java.util.UUID featureId = domain.getFeatureIds().get(i);
            FeatureJpaEntity featureJpa = featureJpaEntities.stream()
                .filter(f -> f.getId().equals(featureId))
                .findFirst()
                .orElse(null);
            
            if (featureJpa != null) {
                CabinFeatureJpaEntity cfJpa = new CabinFeatureJpaEntity();
                cfJpa.setCabin(jpaEntity);
                cfJpa.setFeature(featureJpa);
                cfJpa.setAddedAt(java.time.LocalDateTime.now());
                cabinFeatures.add(cfJpa);
            }
        }
        jpaEntity.setCabinFeatures(cabinFeatures);
        
        // Map policies
        List<PolicyJpaEntity> policyEntities = domain.getPolicies().stream()
            .map(policy -> mapPolicyToJpaEntity(policy, jpaEntity))
            .toList();
        jpaEntity.setPolicies(policyEntities);
        
        return jpaEntity;
    }
    
    // Helper methods for nested objects
    
    private Location mapLocationToDomain(LocationEmbeddable embeddable) {
        if (embeddable == null) return null;
        return new Location(
            embeddable.getCity(),
            embeddable.getState(),
            embeddable.getCountry(),
            embeddable.getAddress(),
            embeddable.getLatitude(),
            embeddable.getLongitude()
        );
    }
    
    private LocationEmbeddable mapLocationToEmbeddable(Location location) {
        if (location == null) return null;
        return new LocationEmbeddable(
            location.getCity(),
            location.getState(),
            location.getCountry(),
            location.getAddress(),
            location.getLatitude(),
            location.getLongitude()
        );
    }
    
    private CabinImage mapImageToDomain(CabinImageJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        return new CabinImage(
            jpaEntity.getId(),
            jpaEntity.getImageUrl(),
            jpaEntity.getDisplayOrder(),
            jpaEntity.getIsPrimary(),
            jpaEntity.getUploadedAt()
        );
    }
    
    private CabinImageJpaEntity mapImageToJpaEntity(CabinImage domain, CabinJpaEntity cabin) {
        if (domain == null) return null;
        CabinImageJpaEntity jpa = new CabinImageJpaEntity();
        jpa.setId(domain.getId());
        jpa.setCabin(cabin);
        jpa.setImageUrl(domain.getImageUrl());
        jpa.setDisplayOrder(domain.getDisplayOrder());
        jpa.setIsPrimary(domain.getIsPrimary());
        jpa.setUploadedAt(domain.getUploadedAt());
        return jpa;
    }
    
    private Policy mapPolicyToDomain(PolicyJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        List<PolicyItem> items = jpaEntity.getItems().stream()
            .map(this::mapPolicyItemToDomain)
            .toList();
        
        return new Policy(
            jpaEntity.getId(),
            jpaEntity.getTitle(),
            jpaEntity.getDisplayOrder(),
            items,
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    private PolicyJpaEntity mapPolicyToJpaEntity(Policy domain, CabinJpaEntity cabin) {
        if (domain == null) return null;
        
        PolicyJpaEntity jpa = new PolicyJpaEntity();
        jpa.setId(domain.getId());
        jpa.setCabin(cabin);
        jpa.setTitle(domain.getTitle());
        jpa.setDisplayOrder(domain.getDisplayOrder());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        
        List<PolicyItemJpaEntity> itemEntities = domain.getItems().stream()
            .map(item -> mapPolicyItemToJpaEntity(item, jpa))
            .toList();
        jpa.setItems(itemEntities);
        
        return jpa;
    }
    
    private PolicyItem mapPolicyItemToDomain(PolicyItemJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        return new PolicyItem(
            jpaEntity.getId(),
            jpaEntity.getDescription(),
            jpaEntity.getDisplayOrder(),
            jpaEntity.getCreatedAt()
        );
    }
    
    private PolicyItemJpaEntity mapPolicyItemToJpaEntity(PolicyItem domain, PolicyJpaEntity policy) {
        if (domain == null) return null;
        PolicyItemJpaEntity jpa = new PolicyItemJpaEntity();
        jpa.setId(domain.getId());
        jpa.setPolicy(policy);
        jpa.setDescription(domain.getDescription());
        jpa.setDisplayOrder(domain.getDisplayOrder());
        jpa.setCreatedAt(domain.getCreatedAt());
        return jpa;
    }
}
