package com.retreatreserve.application.service.category;

import com.retreatreserve.application.command.category.CreateCategoryCommand;
import com.retreatreserve.application.port.in.category.CreateCategoryUseCase;
import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.domain.exception.cabin.CategoryAlreadyExistsException;
import com.retreatreserve.domain.model.cabin.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCategoryService implements CreateCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional
    public Category execute(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.name())) {
            throw new CategoryAlreadyExistsException("Category already exists: " + command.name());
        }
        
        Category category = new Category(
            command.name(),
            command.description(),
            command.imageUrl()
        );
        
        return categoryRepository.save(category);
    }
}
