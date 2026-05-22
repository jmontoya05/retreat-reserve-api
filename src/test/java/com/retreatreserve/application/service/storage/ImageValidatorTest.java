package com.retreatreserve.application.service.storage;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class ImageValidatorTest {

    private final ImageValidator validator = new ImageValidator();

    @Test
    void shouldAcceptValidImage() {
        MockMultipartFile file = new MockMultipartFile(
            "file", "photo.png", "image/png", "bytes".getBytes()
        );

        assertDoesNotThrow(() -> validator.validateImage(file));
    }

    @Test
    void shouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
            "file", "empty.png", "image/png", new byte[0]
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> validator.validateImage(file)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("file is empty"));
    }

    @Test
    void shouldRejectTooLargeFile() {
        // create a payload > 5MB
        byte[] large = new byte[6 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile(
            "file", "big.jpg", "image/jpeg", large
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> validator.validateImage(file)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("file size"));
    }

    @Test
    void shouldRejectInvalidContentType() {
        MockMultipartFile file = new MockMultipartFile(
            "file", "text.txt", "text/plain", "hello".getBytes()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> validator.validateImage(file)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("invalid file type"));
    }
}
