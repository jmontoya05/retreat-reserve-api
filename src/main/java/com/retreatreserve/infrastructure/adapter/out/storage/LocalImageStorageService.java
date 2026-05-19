package com.retreatreserve.infrastructure.adapter.out.storage;

import com.retreatreserve.application.port.out.storage.ImageStorageService;
import com.retreatreserve.infrastructure.exception.storage.ImageDeletionException;
import com.retreatreserve.infrastructure.exception.storage.ImageUploadingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
@Slf4j
public class LocalImageStorageService implements ImageStorageService {
    
    @Value("${storage.local.base-path}")
    private String uploadDirectory;
    
    @Value("${storage.local.base-url}")
    private String baseUrl;
    
    @Override
    public String uploadImage(byte[] imageData, String fileName, String contentType) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String fileExtension = getFileExtension(fileName);
            String key = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(key);
            Files.write(filePath, imageData);
            
            log.info("Image uploaded successfully to local storage: {}", key);
            
            return key;
            
        } catch (IOException e) {
            log.error("Failed to upload image to local storage", e);
            throw new ImageUploadingException("Failed to upload image", e);
        }
    }
    
    @Override
    public void deleteImage(String key) {
        try {
            Path filePath = Paths.get(uploadDirectory).resolve(key);
            
            Files.deleteIfExists(filePath);
            log.info("Image deleted successfully from local storage: {}", key);
            
        } catch (IOException e) {
            log.error("Failed to delete image from local storage", e);
            throw new ImageDeletionException("Failed to delete image", e);
        }
    }
    
    @Override
    public String generatePreSignedUrl(String key, int expirationMinutes) {
        return String.format("%s/images/%s", baseUrl, key);
    }
    
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
}
