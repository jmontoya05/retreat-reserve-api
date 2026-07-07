package com.retreatreserve.application.service.storage;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.retreatreserve.application.dto.storage.UploadedImage;
import com.retreatreserve.application.exception.storage.InvalidImageException;
import com.retreatreserve.application.port.in.storage.UploadImageUseCase;
import com.retreatreserve.application.port.out.storage.ImageStorageService;
import com.retreatreserve.infrastructure.exception.storage.ImageUploadingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageService implements UploadImageUseCase {

    private final ImageStorageService imageStorageService;
    private final ImageValidator imageValidator;

    @Override
    public UploadedImage execute(MultipartFile file) {
        try {
            imageValidator.validateImage(file);
                
            byte[] imageData = file.getBytes();
            String imageKey = imageStorageService.uploadImage(
                imageData,
                file.getOriginalFilename(),
                file.getContentType()
            );

            return new UploadedImage(
                imageKey,
                file.getOriginalFilename(),
                file.getSize()
            );
        } catch (IOException e) {
            log.error("Failed to read image file", e);
            throw new ImageUploadingException("Failed to read image file", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid image file", e);
            throw new InvalidImageException("Invalid image file: " + e.getMessage(), e);
        }
    }
}