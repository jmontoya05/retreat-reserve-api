package com.retreatreserve.infrastructure.adapter.out.persistence.mapper;

import com.retreatreserve.domain.model.reservation.DateRange;
import com.retreatreserve.domain.model.reservation.GuestDetails;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.infrastructure.adapter.out.persistence.embeddable.DateRangeEmbeddable;
import com.retreatreserve.infrastructure.adapter.out.persistence.embeddable.GuestDetailsEmbeddable;
import com.retreatreserve.infrastructure.adapter.out.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    
    public Reservation toDomain(ReservationJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        DateRange dateRange = new DateRange(
            jpaEntity.getDateRange().getCheckInDate(),
            jpaEntity.getDateRange().getCheckOutDate()
        );
        
        GuestDetails guestDetails = new GuestDetails(
            jpaEntity.getGuestDetails().getNumberOfGuests(),
            jpaEntity.getGuestDetails().getGuestName(),
            jpaEntity.getGuestDetails().getGuestPhone()
        );
        
        return new Reservation(
            jpaEntity.getId(),
            jpaEntity.getUser().getId(),
            jpaEntity.getCabin().getId(),
            dateRange,
            guestDetails,
            jpaEntity.getTotalPrice(),
            jpaEntity.getStatus(),
            jpaEntity.getSpecialRequests(),
            jpaEntity.getActive(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
    
    public ReservationJpaEntity toJpaEntity(Reservation domain, UserJpaEntity userJpa, CabinJpaEntity cabinJpa) {
        if (domain == null) return null;
        
        ReservationJpaEntity jpa = new ReservationJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUser(userJpa);
        jpa.setCabin(cabinJpa);
        jpa.setDateRange(new DateRangeEmbeddable(
            domain.getDateRange().getCheckInDate(),
            domain.getDateRange().getCheckOutDate()
        ));
        jpa.setGuestDetails(new GuestDetailsEmbeddable(
            domain.getGuestDetails().getNumberOfGuests(),
            domain.getGuestDetails().getGuestName(),
            domain.getGuestDetails().getGuestPhone()
        ));
        jpa.setTotalPrice(domain.getTotalPrice());
        jpa.setStatus(domain.getStatus());
        jpa.setSpecialRequests(domain.getSpecialRequests());
        jpa.setActive(domain.getActive());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        
        return jpa;
    }
}
