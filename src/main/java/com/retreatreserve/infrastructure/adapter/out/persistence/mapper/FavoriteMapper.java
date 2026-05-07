package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.reservation.Favorite;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteMapper {
    
    public Favorite toDomain(FavoriteJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new Favorite(
            jpaEntity.getId(),
            jpaEntity.getUser().getId(),
            jpaEntity.getCabin().getId(),
            jpaEntity.getAddedAt()
        );
    }
    
    public FavoriteJpaEntity toJpaEntity(Favorite domain, UserJpaEntity userJpa, CabinJpaEntity cabinJpa) {
        if (domain == null) return null;
        
        FavoriteJpaEntity jpa = new FavoriteJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUser(userJpa);
        jpa.setCabin(cabinJpa);
        jpa.setAddedAt(domain.getAddedAt());
        
        return jpa;
    }
}
