package com.retreatreserve.infrastructure.adapter.in.rest.cabin;

import com.retreatreserve.application.command.feature.CreateFeatureCommand;
import com.retreatreserve.application.port.in.feature.CreateFeatureUseCase;
import com.retreatreserve.application.port.in.feature.GetAllFeaturesUseCase;
import com.retreatreserve.domain.model.cabin.Feature;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.CreateFeatureRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response.FeatureResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/features")
@RequiredArgsConstructor
@Tag(name = "Feature Management", description = "Endpoints for creating and retrieving cabin features")
public class FeatureController {
    
    private final GetAllFeaturesUseCase getAllFeaturesUseCase;
    private final CreateFeatureUseCase createFeatureUseCase;
    
    @GetMapping
    public ResponseEntity<List<FeatureResponse>> getAllFeatures() {
        List<Feature> features = getAllFeaturesUseCase.execute();
        return ResponseEntity.ok(
            features.stream()
                .map(f -> new FeatureResponse(
                    f.getId().toString(),
                    f.getName(),
                    f.getIconKey(),
                    f.getDescription()
                ))
                .toList()
        );
    }
    
    @PostMapping
    public ResponseEntity<FeatureResponse> createFeature(@Valid @RequestBody CreateFeatureRequest request) {
        
        CreateFeatureCommand command = new CreateFeatureCommand(request.name(), request.iconKey(), request.description());
        Feature feature = createFeatureUseCase.execute(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new FeatureResponse(
                feature.getId().toString(),
                feature.getName(),
                feature.getIconKey(),
                feature.getDescription()
            )
        );
    }
}
