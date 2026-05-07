package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.ReviewRepository;
import com.retreatreserve.domain.model.reservation.Review;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.*;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    
    private final ReviewJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CabinJpaRepository cabinJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final ReviewMapper mapper;
    
    @Override
    @Transactional
    public Review save(Review review) {
        var userJpa = userJpaRepository.findById(review.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var cabinJpa = cabinJpaRepository.findById(review.getCabinId())
                .orElseThrow(() -> new IllegalArgumentException("Cabin not found"));
        var reservationJpa = reservationJpaRepository.findById(review.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        
        var jpaEntity = mapper.toJpaEntity(review, userJpa, cabinJpa, reservationJpa);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Review> findByCabinId(UUID cabinId) {
        return jpaRepository.findByCabinId(cabinId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Review> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findByReservationId(UUID reservationId) {
        return jpaRepository.findByReservationId(reservationId).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByReservationId(UUID reservationId) {
        return jpaRepository.existsByReservationId(reservationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Review> findActiveByCabinIdOrderByCreatedAtDesc(UUID cabinId) {
        return jpaRepository.findActiveByCabinIdOrderByCreatedAtDesc(cabinId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional
    public void delete(Review review) {
        jpaRepository.deleteById(review.getId());
    }
}
