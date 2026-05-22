package com.retreatreserve.application.service.category;

import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.domain.model.cabin.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCategoriesServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GetAllCategoriesService getAllCategoriesService;

    @Test
    void shouldReturnAllActiveCategories() {
        Category category = new Category(UUID.randomUUID(), "Relaxation", "Rest and restore", "relax-key", true, null, null);
        when(categoryRepository.findAllActive()).thenReturn(List.of(category));

        List<Category> result = getAllCategoriesService.execute();

        assertEquals(1, result.size());
        assertEquals("Relaxation", result.get(0).getName());
    }
}
