package com.retreatreserve.application.service.cabin;

import com.retreatreserve.application.command.cabin.SearchCabinsCommand;
import com.retreatreserve.application.port.in.cabin.SearchCabinsUseCase;
import com.retreatreserve.application.port.out.persistence.CabinRepository;
import com.retreatreserve.domain.model.cabin.Cabin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchCabinsService implements SearchCabinsUseCase {
    
    private final CabinRepository cabinRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Cabin> execute(SearchCabinsCommand command) {
        log.info("Searching cabins with filters - city: {}, maxGuests: {}, maxPrice: {}", 
                 command.city(), command.maxGuests(), command.maxPrice());
        
        if (command.categoryId() != null) {
            UUID categoryId = UUID.fromString(command.categoryId());
            return cabinRepository.findByCategoryId(categoryId);
        }
        
        return cabinRepository.findAvailableCabins(
            command.city(),
            command.maxGuests(),
            command.maxPrice()
        );
    }
}
