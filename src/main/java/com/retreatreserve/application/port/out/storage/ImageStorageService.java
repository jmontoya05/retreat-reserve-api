package com.retreatreserve.application.port.out.storage;

/**
 * Output port for image storage service.
 */
public interface ImageStorageService {
    
    /**
     * Uploads an image and returns the URL.
     */
    String uploadImage(byte[] imageData, String fileName, String contentType);
    
    /**
     * Deletes an image by URL.
     */
    void deleteImage(String imageKey);
    
    /**
     * Generates a pre-signed URL for temporary access (S3).
     */
    String generatePreSignedUrl(String imageKey, int expirationMinutes);
}
