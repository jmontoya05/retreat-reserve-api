package com.retreatreserve.application.service.storage;

import com.retreatreserve.application.dto.storage.UploadedImage;
import com.retreatreserve.application.exception.storage.InvalidImageException;
import com.retreatreserve.application.port.out.storage.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadImageServiceTest {

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private ImageValidator imageValidator;

    @InjectMocks
    private UploadImageService uploadImageService;

    @Test
    void shouldUploadImageSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "test-image.png", "image/png", "bytes".getBytes()
        );

        when(imageStorageService.uploadImage(file.getBytes(), file.getOriginalFilename(), file.getContentType()))
            .thenReturn("image-key");

        UploadedImage result = uploadImageService.execute(file);

        assertEquals("image-key", result.imageKey());
        assertEquals("test-image.png", result.fileName());
        assertEquals(file.getSize(), result.fileSize());
    }

    @Test
    void shouldWrapInvalidImageException() {
        MultipartFile file = new MockMultipartFile(
            "file", "bad-image.png", "image/png", new byte[0]
        );

        doThrow(new IllegalArgumentException("Invalid image"))
            .when(imageValidator).validateImage(file);

        assertThrows(InvalidImageException.class, () -> uploadImageService.execute(file));
    }
}
