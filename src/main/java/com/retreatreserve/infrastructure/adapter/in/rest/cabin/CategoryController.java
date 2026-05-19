package com.retreatreserve.infrastructure.adapter.in.rest.cabin;

import com.retreatreserve.application.command.category.CreateCategoryCommand;
import com.retreatreserve.application.port.in.category.CreateCategoryUseCase;
import com.retreatreserve.application.port.in.category.GetAllCategoriesUseCase;
import com.retreatreserve.application.port.in.storage.GeneratePreSignedUrlUseCase;
import com.retreatreserve.domain.model.cabin.Category;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.request.CreateCategoryRequest;
import com.retreatreserve.infrastructure.adapter.in.rest.cabin.dto.response.CategoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final GetAllCategoriesUseCase getAllCategoriesUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GeneratePreSignedUrlUseCase generatePreSignedUrlUseCase;
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = getAllCategoriesUseCase.execute();
        return ResponseEntity.ok(
            categories.stream()
                .map(c -> new CategoryResponse(
                    c.getId().toString(),
                    c.getName(),
                    c.getDescription(),
                    c.getImageKey()
                ))
                .toList()
        );
    }
    
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        
        CreateCategoryCommand command = new CreateCategoryCommand(request.name(), request.description(), request.imageKey());
        Category category = createCategoryUseCase.execute(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new CategoryResponse(
                category.getId().toString(),
                category.getName(),
                category.getDescription(),
                generatePreSignedUrlUseCase.execute(category.getImageKey())
            )
        );
    }
}
