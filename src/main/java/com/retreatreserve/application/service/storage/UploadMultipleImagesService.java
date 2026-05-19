package com.retreatreserve.application.service.storage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.retreatreserve.application.dto.storage.UploadedImage;
import com.retreatreserve.application.exception.storage.ImageUploadingException;
import com.retreatreserve.application.port.in.storage.UploadMultipleImagesUseCase;
import com.retreatreserve.application.port.out.storage.ImageStorageService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UploadMultipleImagesService implements UploadMultipleImagesUseCase{
    
    private final ImageStorageService imageStorageService;
    private final ImageValidator imageValidator;

    @Override
    public List<UploadedImage> execute(List<MultipartFile> files) {
        
        List<UploadedImage> uploadedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                imageValidator.validateImage(file);
                
                byte[] imageData = file.getBytes();
                String imageKey = imageStorageService.uploadImage(
                    imageData,
                    file.getOriginalFilename(),
                    file.getContentType()
                );
                
                uploadedImages.add(new UploadedImage(
                    imageKey,
                    file.getOriginalFilename(),
                    file.getSize()
                ));
                
            } catch (Exception e) {
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                throw new ImageUploadingException("Failed to read image file", e);
            }
        }

        return uploadedImages;
    }
}
