package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.FavoriteRepository;
import com.retreatreserve.domain.model.reservation.Favorite;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.*;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {
    
    private final FavoriteJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CabinJpaRepository cabinJpaRepository;
    private final FavoriteMapper mapper;
    
    @Override
    @Transactional
    public Favorite save(Favorite favorite) {
        var userJpa = userJpaRepository.findById(favorite.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var cabinJpa = cabinJpaRepository.findById(favorite.getCabinId())
                .orElseThrow(() -> new IllegalArgumentException("Cabin not found"));
        
        var jpaEntity = mapper.toJpaEntity(favorite, userJpa, cabinJpa);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Favorite> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Favorite> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Favorite> findByUserIdAndCabinId(UUID userId, UUID cabinId) {
        return jpaRepository.findByUserIdAndCabinId(userId, cabinId).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndCabinId(UUID userId, UUID cabinId) {
        return jpaRepository.existsByUserIdAndCabinId(userId, cabinId);
    }
    
    @Override
    @Transactional
    public void delete(Favorite favorite) {
        jpaRepository.deleteById(favorite.getId());
    }
    
    @Override
    @Transactional
    public void deleteByUserIdAndCabinId(UUID userId, UUID cabinId) {
        jpaRepository.deleteByUserIdAndCabinId(userId, cabinId);
    }
}
