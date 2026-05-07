package com.retreatreserve.infrastructure.adapter.out.persistence;

import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.domain.model.cabin.Category;
import com.retreatreserve.infrastructure.adapter.out.persistence.jpa.CategoryJpaRepository;
import com.retreatreserve.infrastructure.adapter.out.persistence.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    
    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;
    
    @Override
    @Transactional
    public Category save(Category category) {
        var jpaEntity = mapper.toJpaEntity(category);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
    
    @Override
    @Transactional
    public void delete(Category category) {
        jpaRepository.deleteById(category.getId());
    }
}
