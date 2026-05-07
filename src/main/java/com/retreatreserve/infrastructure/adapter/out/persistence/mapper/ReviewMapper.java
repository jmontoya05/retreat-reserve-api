package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.reservation.Rating;
import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {
    
    public Review toDomain(ReviewJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        Rating rating = new Rating(jpaEntity.getRating());
        
        return new Review(
            jpaEntity.getId(),
            jpaEntity.getUser().getId(),
            jpaEntity.getCabin().getId(),
            jpaEntity.getReservation().getId(),
            rating,
            jpaEntity.getComment(),
            jpaEntity.getActive(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    public ReviewJpaEntity toJpaEntity(Review domain, UserJpaEntity userJpa, 
                                      CabinJpaEntity cabinJpa, ReservationJpaEntity reservationJpa) {
        if (domain == null) return null;
        
        ReviewJpaEntity jpa = new ReviewJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUser(userJpa);
        jpa.setCabin(cabinJpa);
        jpa.setReservation(reservationJpa);
        jpa.setRating(domain.getRating().getValue());
        jpa.setComment(domain.getComment());
        jpa.setActive(domain.getActive());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        
        return jpa;
    }
}
