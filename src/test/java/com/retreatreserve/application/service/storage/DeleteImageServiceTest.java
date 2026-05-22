package com.retreatreserve.application.service.storage;

import com.retreatreserve.application.port.out.storage.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteImageServiceTest {

    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private DeleteImageService deleteImageService;

    @Test
    void shouldDeleteImageByKey() {
        String imageKey = "image-key";

        deleteImageService.execute(imageKey);

        verify(imageStorageService).deleteImage(imageKey);
    }
}
