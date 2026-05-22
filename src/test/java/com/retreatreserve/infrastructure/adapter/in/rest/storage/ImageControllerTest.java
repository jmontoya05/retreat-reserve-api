package com.retreatreserve.infrastructure.adapter.in.rest.storage;

import com.retreatreserve.application.dto.storage.UploadedImage;
import com.retreatreserve.application.port.in.storage.DeleteImageUseCase;
import com.retreatreserve.application.port.in.storage.UploadImageUseCase;
import com.retreatreserve.application.port.in.storage.UploadMultipleImagesUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = ImageControllerTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UploadImageUseCase uploadImageUseCase;

    @MockitoBean
    private UploadMultipleImagesUseCase uploadMultipleImagesUseCase;

    @MockitoBean
    private DeleteImageUseCase deleteImageUseCase;

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @Import({ImageController.class})
    static class TestConfig {
    }

    @Test
    void shouldUploadSingleImageSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-image.jpg",
            "image/jpeg",
            "image-data".getBytes()
        );

        when(uploadImageUseCase.execute(any())).thenReturn(new UploadedImage("image-key", "test-image.jpg", 10));

        mockMvc.perform(multipart("/images/upload").file(file))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.imageKey").value("image-key"))
            .andExpect(jsonPath("$.fileName").value("test-image.jpg"));
    }

    @Test
    void shouldUploadMultipleImagesSuccessfully() throws Exception {
        MockMultipartFile fileOne = new MockMultipartFile(
            "files",
            "image-one.jpg",
            "image/jpeg",
            "image-one".getBytes()
        );
        MockMultipartFile fileTwo = new MockMultipartFile(
            "files",
            "image-two.jpg",
            "image/jpeg",
            "image-two".getBytes()
        );

        when(uploadMultipleImagesUseCase.execute(any())).thenReturn(List.of(
            new UploadedImage("key-1", "image-one.jpg", 8),
            new UploadedImage("key-2", "image-two.jpg", 9)
        ));

        mockMvc.perform(multipart("/images/upload-multiple").file(fileOne).file(fileTwo))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].imageKey").value("key-1"))
            .andExpect(jsonPath("$[1].fileName").value("image-two.jpg"));
    }

    @Test
    void shouldDeleteImageSuccessfully() throws Exception {
        mockMvc.perform(delete("/images").param("imageKey", "image-key"))
            .andExpect(status().isNoContent());

        verify(deleteImageUseCase).execute("image-key");
    }
}
