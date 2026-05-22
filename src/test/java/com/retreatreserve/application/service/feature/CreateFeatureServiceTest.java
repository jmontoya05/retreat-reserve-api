package com.retreatreserve.application.service.feature;

import com.retreatreserve.application.command.feature.CreateFeatureCommand;
import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.exception.cabin.FeatureAlreadyExistsException;
import com.retreatreserve.domain.model.cabin.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFeatureServiceTest {

    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    private CreateFeatureService createFeatureService;

    @Test
    void shouldCreateFeatureWhenNameIsUnique() {
        CreateFeatureCommand command = new CreateFeatureCommand("Sauna", "sauna-key", "Heated sauna");
        Feature savedFeature = new Feature("Sauna", "sauna-key", "Heated sauna");

        when(featureRepository.existsByName("Sauna")).thenReturn(false);
        when(featureRepository.save(any(Feature.class))).thenReturn(savedFeature);

        Feature result = createFeatureService.execute(command);

        assertNotNull(result);
        assertEquals("Sauna", result.getName());
        assertEquals("sauna-key", result.getIconKey());
    }

    @Test
    void shouldThrowWhenFeatureNameAlreadyExists() {
        CreateFeatureCommand command = new CreateFeatureCommand("Sauna", "sauna-key", "Heated sauna");

        when(featureRepository.existsByName("Sauna")).thenReturn(true);

        assertThrows(FeatureAlreadyExistsException.class, () -> createFeatureService.execute(command));
    }
}
