package com.retreatreserve.infrastructure.adapter.out.storage;

import com.retreatreserve.application.port.out.storage.ImageStorageService;
import com.retreatreserve.infrastructure.exception.storage.GeneratePreSignedUrlException;
import com.retreatreserve.infrastructure.exception.storage.ImageDeletionException;
import com.retreatreserve.infrastructure.exception.storage.ImageUploadingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
@RequiredArgsConstructor
@Slf4j
public class S3ImageStorageService implements ImageStorageService {
    
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    
    @Value("${storage.s3.bucket-name}")
    private String bucketName;
    
    @Value("${storage.s3.region}")
    private String region;
    
    @Override
    public String uploadImage(byte[] imageData, String fileName, String contentType) {
        try {
            String fileExtension = getFileExtension(fileName);
            String key = "cabin-images/" + UUID.randomUUID().toString() + fileExtension;
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();
            
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));            
            log.info("Image uploaded successfully to S3: {}", key);
            return key;
            
        } catch (S3Exception e) {
            log.error("Failed to upload image to S3", e);
            throw new ImageUploadingException("Failed to upload image to S3", e);
        }
    }
    
    @Override
    public void deleteImage(String imageKey) {
        try {            
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(imageKey)
                .build();
            
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Image deleted successfully from S3: {}", imageKey);
            
        } catch (S3Exception e) {
            log.error("Failed to delete image from S3", e);
            throw new ImageDeletionException("Failed to delete image from S3", e);
        }
    }
    
    @Override
    public String generatePreSignedUrl(String imageKey, int expirationMinutes) {
        try {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .getObjectRequest(builder -> builder
                    .bucket(bucketName)
                    .key(imageKey))
                .build();
            
            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            
            return presignedRequest.url().toString();
            
        } catch (S3Exception e) {
            log.error("Failed to generate pre-signed URL", e);
            throw new GeneratePreSignedUrlException("Failed to generate pre-signed URL", e);
        }
    }
    
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
}
