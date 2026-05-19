package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.cabin.Category;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.CategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    
    public Category toDomain(CategoryJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new Category(
            jpaEntity.getId(),
            jpaEntity.getName(),
            jpaEntity.getDescription(),
            jpaEntity.getImageKey(),
            jpaEntity.getActive(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    public CategoryJpaEntity toJpaEntity(Category domain) {
        if (domain == null) return null;
        
        CategoryJpaEntity jpaEntity = new CategoryJpaEntity();
        updateJpaEntity(domain, jpaEntity);
        return jpaEntity;
    }
    
    public void updateJpaEntity(Category domain, CategoryJpaEntity jpaEntity) {
        if (domain == null || jpaEntity == null) return;
        
        jpaEntity.setId(domain.getId());
        jpaEntity.setName(domain.getName());
        jpaEntity.setDescription(domain.getDescription());
        jpaEntity.setImageKey(domain.getImageKey());
        jpaEntity.setActive(domain.getActive());
        jpaEntity.setCreatedAt(domain.getCreatedAt());
        jpaEntity.setUpdatedAt(domain.getUpdatedAt());
    }
}
