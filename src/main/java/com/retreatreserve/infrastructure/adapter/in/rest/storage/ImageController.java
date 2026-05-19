package com.retreatreserve.infrastructure.adapter.in.rest.storage;

import com.retreatreserve.application.dto.storage.UploadedImage;
import com.retreatreserve.application.port.in.storage.DeleteImageUseCase;
import com.retreatreserve.application.port.in.storage.UploadImageUseCase;
import com.retreatreserve.application.port.in.storage.UploadMultipleImagesUseCase;
import com.retreatreserve.infrastructure.adapter.in.rest.storage.dto.response.ImageUploadResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    
    private final UploadImageUseCase uploadImageUseCase;
    private final UploadMultipleImagesUseCase uploadMultipleImagesUseCase;
    private final DeleteImageUseCase deleteImageUseCase;
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        UploadedImage uploadedImage = uploadImageUseCase.execute(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ImageUploadResponse(
            uploadedImage.imageKey(),
            uploadedImage.fileName(),
            uploadedImage.fileSize()
        ));
    }
    
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ImageUploadResponse>> uploadMultipleImages(
            @RequestParam("files") List<MultipartFile> files) {
        
        List<UploadedImage> uploadedImages =  uploadMultipleImagesUseCase.execute(files);
        List<ImageUploadResponse> responses = uploadedImages.stream()
            .map(img -> new ImageUploadResponse(
                img.imageKey(),
                img.fileName(),
                img.fileSize()
            ))
            .toList();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
    
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImage(@RequestParam String imageKey) {
        deleteImageUseCase.execute(imageKey);
        return ResponseEntity.noContent().build();
    }
}
