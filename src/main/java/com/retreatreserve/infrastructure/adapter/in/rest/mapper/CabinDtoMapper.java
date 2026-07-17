package com.retreatreserve.infrastructure.adapter.in.rest.mapper;

import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response.CabinResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CabinDtoMapper {
    
    public CabinResponse toResponse(Cabin cabin, List<String> imageUrls) {
            
        return new CabinResponse(
            cabin.getId().toString(),
            cabin.getName(),
            cabin.getDescription(),
            cabin.getCategoryId().toString(),
            new CabinResponse.LocationResponse(
                cabin.getLocation().getCity(),
                cabin.getLocation().getState(),
                cabin.getLocation().getCountry()
            ),
            cabin.getCapacity().getMaxGuests(),
            cabin.getNumberOfBedrooms(),
            cabin.getNumberOfBathrooms(),
            cabin.getPricePerNight(),
            cabin.getAverageRating(),
            cabin.getTotalReviews(),
            cabin.getStatus().name(),
            imageUrls,
            cabin.getFeatureIds().stream().map(Object::toString).toList(),
            cabin.getPolicies() != null ? cabin.getPolicies().stream()
                .map(p -> new CabinResponse.PolicyResponse(
                    p.getId() != null ? p.getId().toString() : null,
                    p.getTitle(),
                    p.getDisplayOrder(),
                    p.getItems() != null ? p.getItems().stream()
                        .map(i -> new CabinResponse.PolicyItemResponse(
                            i.getId() != null ? i.getId().toString() : null,
                            i.getDescription(),
                            i.getDisplayOrder()
                        ))
                        .toList() : List.of()
                ))
                .toList() : List.of()
        );
    }
}
