package com.retreatreserve.application.dto.storage;

public record UploadedImage(
    String imageKey,
    String fileName,
    long fileSize
) {}
