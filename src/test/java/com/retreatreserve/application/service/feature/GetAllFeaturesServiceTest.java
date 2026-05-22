package com.retreatreserve.application.service.feature;

import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.model.cabin.Feature;
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
class GetAllFeaturesServiceTest {

    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    private GetAllFeaturesService getAllFeaturesService;

    @Test
    void shouldReturnAllActiveFeatures() {
        Feature feature = new Feature(
            UUID.randomUUID(),
            "Hot Tub",
            "hot-tub-icon.png",
            "Relaxing hot tub",
            true,
            null,
            null
        );
        when(featureRepository.findAllActive()).thenReturn(List.of(feature));

        List<Feature> result = getAllFeaturesService.execute();

        assertEquals(1, result.size());
        assertEquals(feature, result.get(0));
    }
}
