package com.retreatreserve.application.service.category;

import com.retreatreserve.application.port.in.category.GetAllCategoriesUseCase;
import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.domain.model.cabin.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCategoriesService implements GetAllCategoriesUseCase {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> execute() {
        return categoryRepository.findAllActive();
    }
}
