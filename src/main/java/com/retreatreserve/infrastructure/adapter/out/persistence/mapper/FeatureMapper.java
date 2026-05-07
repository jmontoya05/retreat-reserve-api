package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.cabin.Feature;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.FeatureJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FeatureMapper {
    
    public Feature toDomain(FeatureJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new Feature(
            jpaEntity.getId(),
            jpaEntity.getName(),
            jpaEntity.getIconUrl(),
            jpaEntity.getDescription(),
            jpaEntity.getActive(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    public FeatureJpaEntity toJpaEntity(Feature domain) {
        if (domain == null) return null;
        
        FeatureJpaEntity jpaEntity = new FeatureJpaEntity();
        updateJpaEntity(domain, jpaEntity);
        return jpaEntity;
    }
    
    public void updateJpaEntity(Feature domain, FeatureJpaEntity jpaEntity) {
        if (domain == null || jpaEntity == null) return;
        
        jpaEntity.setId(domain.getId());
        jpaEntity.setName(domain.getName());
        jpaEntity.setIconUrl(domain.getIconUrl());
        jpaEntity.setDescription(domain.getDescription());
        jpaEntity.setActive(domain.getActive());
        jpaEntity.setCreatedAt(domain.getCreatedAt());
        jpaEntity.setUpdatedAt(domain.getUpdatedAt());
    }
}
