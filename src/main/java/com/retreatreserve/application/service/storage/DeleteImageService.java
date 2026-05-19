package com.retreatreserve.application.service.storage;

import org.springframework.stereotype.Service;

import com.retreatreserve.application.port.in.storage.DeleteImageUseCase;
import com.retreatreserve.application.port.out.storage.ImageStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteImageService implements DeleteImageUseCase {
    
    private final ImageStorageService imageStorageService;

    @Override
    public void execute(String imageKey) {
        imageStorageService.deleteImage(imageKey);
    }
}
