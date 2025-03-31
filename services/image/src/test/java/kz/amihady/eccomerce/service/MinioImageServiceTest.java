package kz.amihady.eccomerce.service;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import kz.amihady.eccomerce.exception.MinioException;
import kz.amihady.eccomerce.minio.config.MinioProperties;
import kz.amihady.eccomerce.minio.service.MinioImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioImageServiceTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @InjectMocks
    private MinioImageService minioImageService;

    private UUID imageId;

    @BeforeEach
    public void setUp(){
        imageId=UUID.randomUUID();
    }

    @Test
    void uploadImage_Success() throws Exception {
        String bucketName = "test-bucket";
        String minioUrl = "http://minio.example.com";
        String expectedUrl = minioUrl + "/" + bucketName + "/" + imageId;

        MultipartFile file = new MockMultipartFile("image.jpg", "image.jpg",
                "image/jpeg", "fake-image-content".getBytes());

        when(minioProperties.getBucketName()).thenReturn(bucketName);
        when(minioProperties.getUrl()).thenReturn(minioUrl);

        String imageUrl = minioImageService.uploadImage(file, imageId);

        verify(minioClient).putObject(any(PutObjectArgs.class));
        assertEquals(expectedUrl, imageUrl);
    }

    @Test
    void uploadImage_Failure() throws Exception {
        MultipartFile file = new MockMultipartFile("image.jpg", "image.jpg",
                "image/jpeg", "fake-image-content".getBytes());

        lenient().doThrow(new RuntimeException("MinIO error"))
                .when(minioClient).putObject(any(PutObjectArgs.class));

        MinioException exception = assertThrows(MinioException.class,
                () -> minioImageService.uploadImage(file, imageId));

        assertEquals("Ошибка при добавлении изображение в MinIO", exception.getMessage());
    }

    @Test
    void deleteImage_Success() throws Exception {
        String bucketName = "test-bucket";

        when(minioProperties.getBucketName()).thenReturn(bucketName);

        minioImageService.deleteImage(imageId);

        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }
}
