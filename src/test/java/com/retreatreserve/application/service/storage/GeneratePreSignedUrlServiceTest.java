package com.retreatreserve.application.service.storage;

import com.retreatreserve.application.port.out.storage.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratePreSignedUrlServiceTest {

    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private GeneratePreSignedUrlService generatePreSignedUrlService;

    @Test
    void shouldReturnPreSignedUrl() {
        String key = "image-key";
        String expectedUrl = "https://example.com/presigned-url";

        when(imageStorageService.generatePreSignedUrl(key, 60)).thenReturn(expectedUrl);

        assertEquals(expectedUrl, generatePreSignedUrlService.execute(key));
    }
}
