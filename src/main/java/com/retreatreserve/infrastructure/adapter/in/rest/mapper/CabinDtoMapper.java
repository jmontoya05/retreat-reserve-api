package com.retreatreserve.infrastructure.adapter.in.rest.mapper;

import com.retreatreserve.domain.model.cabin.Cabin;
import com.retreatreserve.domain.model.cabin.CabinImage;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response.CabinResponse;
import org.springframework.stereotype.Component;

@Component
public class CabinDtoMapper {
    
    public CabinResponse toResponse(Cabin cabin) {
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
            cabin.getImages().stream().map(CabinImage::getImageUrl).toList(),
            cabin.getFeatureIds().stream().map(Object::toString).toList()
        );
    }
}
