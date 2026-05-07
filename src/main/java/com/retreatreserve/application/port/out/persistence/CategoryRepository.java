package com.retreatreserve.application.port.out.persistence;

import com.retreatreserve.domain.model.cabin.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Category aggregate.
 */
public interface CategoryRepository {
    
    Category save(Category category);
    
    Optional<Category> findById(UUID id);
    
    Optional<Category> findByName(String name);
    
    List<Category> findAllActive();
    
    boolean existsByName(String name);
    
    void delete(Category category);
}
