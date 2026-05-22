package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.command.cabin.CreateCabinCommand;
import com.retreatreserve.application.command.cabin.CreateCabinCommand.PolicyInput;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.exception.cabin.CategoryNotFoundException;
import com.retreatreserve.domain.exception.cabin.FeatureNotFoundException;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.Category;
import com.retreatreserve.domain.model.cabin.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCabinServiceTest {

    @Mock
    private CabinRepository cabinRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    private CreateCabinService createCabinService;

    @Test
    void shouldCreateCabinWhenCategoryAndFeaturesExist() {
        UUID categoryId = UUID.randomUUID();
        UUID featureId = UUID.randomUUID();

        CreateCabinCommand command = new CreateCabinCommand(
            "Ocean View",
            "Beautiful cabin",
            categoryId.toString(),
            "City", "State", "Country", "Address",
            new BigDecimal("10.0"), new BigDecimal("20.0"),
            4, 2, 1,
            new BigDecimal("250"),
            List.of("image-key-1"),
            List.of(featureId.toString()),
            List.of(new PolicyInput("No smoking", 0, List.of("No smoking indoors")))
        );

        Category category = new Category("Nature", "Nature description","nature-key");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(featureRepository.findByIds(List.of(featureId))).thenReturn(List.of(new Feature("Pool", "pool-key", "Pool access")));
        when(cabinRepository.save(any(Cabin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cabin saved = createCabinService.execute(command);

        assertNotNull(saved);
        assertEquals(categoryId, saved.getCategoryId());
        assertEquals(1, saved.getImages().size());
        assertEquals(1, saved.getFeatureIds().size());
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();
        CreateCabinCommand command = new CreateCabinCommand(
            "Ocean View", "Beautiful cabin", categoryId.toString(),
            "City", "State", "Country", "Address",
            new BigDecimal("10.0"), new BigDecimal("20.0"),
            4, 2, 1, new BigDecimal("250"),
            List.of(), List.of(), null
        );

        assertThrows(CategoryNotFoundException.class, () -> createCabinService.execute(command));
    }

    @Test
    void shouldThrowWhenFeatureMissing() {
        UUID categoryId = UUID.randomUUID();
        UUID featureId = UUID.randomUUID();

        CreateCabinCommand command = new CreateCabinCommand(
            "Ocean View", "Beautiful cabin", categoryId.toString(),
            "City", "State", "Country", "Address",
            new BigDecimal("10.0"), new BigDecimal("20.0"),
            4, 2, 1, new BigDecimal("250"),
            List.of(), List.of(featureId.toString()), null
        );

        Category category = new Category("Nature", "Nature description","nature-key");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(featureRepository.findByIds(List.of(featureId))).thenReturn(List.of());

        assertThrows(FeatureNotFoundException.class, () -> createCabinService.execute(command));
    }
}
