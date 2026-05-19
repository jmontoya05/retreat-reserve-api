package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.command.cabin.CreateCabinCommand;
import com.retreatreserve.application.command.cabin.CreateCabinCommand.PolicyInput;
import com.retreatreserve.application.port.in.cabin.CreateCabinUseCase;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.application.port.out.persistence.CategoryRepository;
import com.retreatreserve.application.port.out.persistence.FeatureRepository;
import com.retreatreserve.domain.exception.cabin.CategoryNotFoundException;
import com.retreatreserve.domain.exception.cabin.FeatureNotFoundException;
import com.retreatreserve.domain.model.cabin.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use case for creating a cabin with all its children (images, features, policies).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCabinService implements CreateCabinUseCase {
    
    private final CabinRepository cabinRepository;
    private final CategoryRepository categoryRepository;
    private final FeatureRepository featureRepository;
    
    @Override
    @Transactional
    public Cabin execute(CreateCabinCommand command) {
        log.info("Creating cabin: {}", command.name());
        
        UUID categoryId = UUID.fromString(command.categoryId());
        categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + command.categoryId()));
        
        List<UUID> featureIds = command.featureIds().stream()
            .map(UUID::fromString)
            .toList();
        
        List<Feature> features = featureRepository.findByIds(featureIds);
        if (features.size() != featureIds.size()) {
            throw new FeatureNotFoundException("One or more features not found");
        }
        
        Location location = new Location(
            command.city(),
            command.state(),
            command.country(),
            command.address(),
            command.latitude(),
            command.longitude()
        );
        
        Capacity capacity = new Capacity(command.maxGuests());
        
        Cabin cabin = new Cabin(
            command.name(),
            command.description(),
            categoryId,
            location,
            capacity,
            command.numberOfBedrooms(),
            command.numberOfBathrooms(),
            command.pricePerNight()
        );
        
        addImages(command.imageKeys(), cabin);        
        featureIds.forEach(cabin::addFeature);
        addPolicies(command.policies(), cabin);        
        Cabin savedCabin = cabinRepository.save(cabin);
        
        log.info("Cabin created successfully: {}", savedCabin.getId());
        return savedCabin;
    }

    private void addImages(List<String> imageKeys, Cabin cabin) {
        if (imageKeys != null && !imageKeys.isEmpty()) {
            for (int i = 0; i < imageKeys.size(); i++) {
                CabinImage image = new CabinImage(
                    imageKeys.get(i),
                    i,
                    i == 0
                );
                
                cabin.addImage(image);
            }
        }
    }

    private void addPolicies(List<PolicyInput> policyInputs, Cabin cabin) {
        if (policyInputs != null && !policyInputs.isEmpty()) {
            for (PolicyInput policyInput : policyInputs) {
                Policy policy = new Policy(policyInput.title(), policyInput.displayOrder());
                
                if (policyInput.items() != null) {
                    for (int i = 0; i < policyInput.items().size(); i++) {
                        PolicyItem item = new PolicyItem(policyInput.items().get(i), i);
                        policy.addItem(item);
                    }
                }
                
                cabin.addPolicy(policy);
            }
        }
    }
}
