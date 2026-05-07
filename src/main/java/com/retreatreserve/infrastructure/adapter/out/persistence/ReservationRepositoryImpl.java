package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.ReservationRepository;
import com.retreatreserve.domain.model.reservation.Reservation;
import com.retreatreserve.domain.model.reservation.ReservationStatus;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.*;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {
    
    private final ReservationJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CabinJpaRepository cabinJpaRepository;
    private final ReservationMapper mapper;
    
    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        var userJpa = userJpaRepository.findById(reservation.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var cabinJpa = cabinJpaRepository.findById(reservation.getCabinId())
                .orElseThrow(() -> new IllegalArgumentException("Cabin not found"));
        
        var jpaEntity = mapper.toJpaEntity(reservation, userJpa, cabinJpa);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByCabinId(UUID cabinId) {
        return jpaRepository.findByCabinId(cabinId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByUserIdAndStatus(UUID userId, ReservationStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findOverlappingReservations(UUID cabinId, LocalDate checkInDate, LocalDate checkOutDate) {
        return jpaRepository.findOverlappingReservations(cabinId, checkInDate, checkOutDate).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findReservationsToComplete(LocalDate currentDate) {
        return jpaRepository.findReservationsToComplete(currentDate).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional
    public void delete(Reservation reservation) {
        jpaRepository.deleteById(reservation.getId());
    }
}
