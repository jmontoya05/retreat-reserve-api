package com.retreatreserve.application.port.in.category;

import com.retreatreserve.application.command.category.CreateCategoryCommand;
import com.retreatreserve.domain.model.cabin.Category;

public interface CreateCategoryUseCase {
    Category execute(CreateCategoryCommand command);
}
