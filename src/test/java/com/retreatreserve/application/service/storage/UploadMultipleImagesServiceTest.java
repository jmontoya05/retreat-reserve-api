package com.retreatreserve.application.service.storage;

import com.retreatreserve.application.dto.storage.UploadedImage;
import com.retreatreserve.application.exception.storage.ImageUploadingException;
import com.retreatreserve.application.port.out.storage.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadMultipleImagesServiceTest {

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private ImageValidator imageValidator;

    @InjectMocks
    private UploadMultipleImagesService uploadMultipleImagesService;

    @Test
    void shouldUploadMultipleImagesSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "test-image.png", "image/png", "bytes".getBytes()
        );

        when(imageStorageService.uploadImage(file.getBytes(), file.getOriginalFilename(), file.getContentType()))
            .thenReturn("image-key");

        List<UploadedImage> result = uploadMultipleImagesService.execute(List.of(file));

        assertEquals(1, result.size());
        assertEquals("image-key", result.get(0).imageKey());
    }

    @Test
    void shouldFailWhenAnyImageUploadFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "bad-image.png", "image/png", "bytes".getBytes()
        );
        List<MultipartFile> files = List.of(file);
        
        doThrow(new RuntimeException("upload failed"))
            .when(imageStorageService).uploadImage(file.getBytes(), file.getOriginalFilename(), file.getContentType());

        assertThrows(ImageUploadingException.class, () -> uploadMultipleImagesService.execute(files));
    }
}
