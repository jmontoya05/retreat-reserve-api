package com.retreatreserve.application.port.in.storage;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.retreatreserve.application.dto.storage.UploadedImage;

public interface UploadMultipleImagesUseCase {
    List<UploadedImage> execute(List<MultipartFile> files);
}
