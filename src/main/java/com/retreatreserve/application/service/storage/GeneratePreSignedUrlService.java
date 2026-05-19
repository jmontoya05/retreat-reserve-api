package com.retreatreserve.application.service.storage;

import org.springframework.stereotype.Service;

import com.retreatreserve.application.port.in.storage.GeneratePreSignedUrlUseCase;
import com.retreatreserve.application.port.out.storage.ImageStorageService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GeneratePreSignedUrlService implements GeneratePreSignedUrlUseCase{
    
    private final ImageStorageService imageStorageService;
    private static final int EXPIRATION_MINUTES = 60; //1 hour expiration time for pre-signed URLs

    @Override
    public String execute(String key) {
        return imageStorageService.generatePreSignedUrl(key, EXPIRATION_MINUTES);
    }
}
