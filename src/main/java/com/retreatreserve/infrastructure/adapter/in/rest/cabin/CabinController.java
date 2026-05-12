package com.retreatreserve.infrastructure.adapter.in.rest.cabin;

import com.retreatreserve.application.command.cabin.CreateCabinCommand;
import com.retreatreserve.application.command.cabin.SearchCabinsCommand;
import com.retreatreserve.application.port.in.cabin.CreateCabinUseCase;
import com.retreatreserve.application.port.in.cabin.GetCabinDetailsUseCase;
import com.retreatreserve.application.port.in.cabin.SearchCabinsUseCase;
import com.retreatreserve.application.port.in.cabin.UpdateCabinPricingUseCase;
import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.CreateCabinRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.UpdatePricingRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response.CabinResponse;
import com.retreatreserve.infrastructure.adapter.in.rest.mapper.CabinDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cabins")
@RequiredArgsConstructor
public class CabinController {
    
    private final CreateCabinUseCase createCabinUseCase;
    private final SearchCabinsUseCase searchCabinsUseCase;
    private final GetCabinDetailsUseCase getCabinDetailsUseCase;
    private final UpdateCabinPricingUseCase updateCabinPricingUseCase;
    private final CabinDtoMapper cabinDtoMapper;
    
    @PostMapping
    public ResponseEntity<CabinResponse> createCabin(@Valid @RequestBody CreateCabinRequest request) {
        CreateCabinCommand command = mapCabinCommand(request);
        
        Cabin cabin = createCabinUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(cabinDtoMapper.toResponse(cabin));
    }
    
    @GetMapping("/{cabinId}")
    public ResponseEntity<CabinResponse> getCabin(@PathVariable String cabinId) {
        Cabin cabin = getCabinDetailsUseCase.execute(cabinId);
        return ResponseEntity.ok(cabinDtoMapper.toResponse(cabin));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CabinResponse>> searchCabins(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer maxGuests,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String categoryId) {
        
        SearchCabinsCommand command = new SearchCabinsCommand(city, maxGuests, maxPrice, categoryId);
        List<Cabin> cabins = searchCabinsUseCase.execute(command);
        
        return ResponseEntity.ok(
            cabins.stream()
                .map(cabinDtoMapper::toResponse)
                .collect(Collectors.toList())
        );
    }
    
    @PatchMapping("/{cabinId}/pricing")
    public ResponseEntity<CabinResponse> updatePricing(
            @PathVariable String cabinId,
            @Valid @RequestBody UpdatePricingRequest request) {
        
        Cabin cabin = updateCabinPricingUseCase.execute(cabinId, request.newPricePerNight());
        return ResponseEntity.ok(cabinDtoMapper.toResponse(cabin));
    }

    private CreateCabinCommand mapCabinCommand(CreateCabinRequest request) {
        return new CreateCabinCommand(
            request.name(), request.description(), request.categoryId(),
            request.city(), request.state(), request.country(),
            request.address(), request.latitude(), request.longitude(),
            request.maxGuests(), request.numberOfBedrooms(), request.numberOfBathrooms(),
            request.pricePerNight(), request.imageUrls(), request.featureIds(),
            request.policies() != null ? request.policies().stream()
                .map(p -> new CreateCabinCommand.PolicyInput(p.title(), p.displayOrder(), p.items()))
                .toList() : null
        );
    }
}
