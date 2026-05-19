package com.retreatreserve.infrastructure.adapter.in.rest.storage.dto.response;

public record ImageUploadResponse(
    String imageKey,
    String fileName,
    long fileSize
) {}
