package com.retreatreserve.application.port.in.category;

import java.util.List;

import com.retreatreserve.domain.model.cabin.Category;

public interface GetAllCategoriesUseCase {
    List<Category> execute();
}
