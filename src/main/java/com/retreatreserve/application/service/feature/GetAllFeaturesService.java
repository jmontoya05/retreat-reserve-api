package com.retreatreserve.application.service.feature;

import com.retreatreserve.application.port.in.feature.GetAllFeaturesUseCase;
import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.model.cabin.Feature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllFeaturesService implements GetAllFeaturesUseCase {
    
    private final FeatureRepository featureRepository;
    
    @Transactional(readOnly = true)
    public List<Feature> execute() {
        return featureRepository.findAllActive();
    }
}
