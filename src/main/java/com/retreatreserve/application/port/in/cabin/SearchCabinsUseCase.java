package com.retreatreserve.application.port.in.cabin;

import java.util.List;

import com.retreatreserve.application.command.cabin.SearchCabinsCommand;
import com.retreatreserve.domain.model.cabin.Cabin;

public interface SearchCabinsUseCase {
    List<Cabin> execute(SearchCabinsCommand command);
}
