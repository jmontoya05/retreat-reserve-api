package com.retreatreserve.application.port.in.storage;

public interface GeneratePreSignedUrlUseCase {
    String execute(String key);
}
