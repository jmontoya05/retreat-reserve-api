package com.retreatreserve.application.service.category;

import com.retreatreserve.application.command.category.CreateCategoryCommand;
import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.domain.exception.cabin.CategoryAlreadyExistsException;
import com.retreatreserve.domain.model.cabin.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryService createCategoryService;

    @Test
    void shouldCreateCategoryWhenNameIsUnique() {
        CreateCategoryCommand command = new CreateCategoryCommand("Adventure", "Adrenaline-filled stays", "adventure-key");
        Category savedCategory = new Category("Adventure", "Adrenaline-filled stays", "adventure-key");

        when(categoryRepository.existsByName("Adventure")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category result = createCategoryService.execute(command);

        assertNotNull(result);
        assertEquals("Adventure", result.getName());
        assertEquals("adventure-key", result.getImageKey());
    }

    @Test
    void shouldThrowWhenCategoryNameAlreadyExists() {
        CreateCategoryCommand command = new CreateCategoryCommand("Adventure", "Adrenaline-filled stays", "adventure-key");

        when(categoryRepository.existsByName("Adventure")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class, () -> createCategoryService.execute(command));
    }
}
