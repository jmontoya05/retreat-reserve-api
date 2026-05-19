package com.retreatreserve.application.port.in.storage;

import org.springframework.web.multipart.MultipartFile;

import com.retreatreserve.application.dto.storage.UploadedImage;

public interface UploadImageUseCase {
    UploadedImage execute(MultipartFile file);
}
