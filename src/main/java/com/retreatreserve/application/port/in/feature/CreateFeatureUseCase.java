package com.retreatreserve.application.port.in.feature;

import com.retreatreserve.application.command.feature.CreateFeatureCommand;
import com.retreatreserve.domain.model.cabin.Feature;

public interface CreateFeatureUseCase {
    Feature execute(CreateFeatureCommand command);
}
