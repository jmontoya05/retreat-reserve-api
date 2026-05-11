package com.retreatreserve.application.port.in.feature;

import java.util.List;

import com.retreatreserve.domain.model.cabin.Feature;

public interface GetAllFeaturesUseCase {
    List<Feature> execute();
}
