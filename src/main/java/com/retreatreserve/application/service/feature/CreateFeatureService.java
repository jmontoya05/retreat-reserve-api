package com.retreatreserve.application.service.feature;

import com.retreatreserve.application.command.feature.CreateFeatureCommand;
import com.retreatreserve.application.port.in.feature.CreateFeatureUseCase;
import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.exception.cabin.FeatureAlreadyExistsException;
import com.retreatreserve.domain.model.cabin.Feature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateFeatureService implements CreateFeatureUseCase {
    
    private final FeatureRepository featureRepository;
    
    @Override
    @Transactional
    public Feature execute(CreateFeatureCommand command) {
        if (featureRepository.existsByName(command.name())) {
            throw new FeatureAlreadyExistsException("Feature already exists: " + command.name());
        }
        
        Feature feature = new Feature(
            command.name(),
            command.iconUrl(),
            command.description()
        );
        
        return featureRepository.save(feature);
    }
}
