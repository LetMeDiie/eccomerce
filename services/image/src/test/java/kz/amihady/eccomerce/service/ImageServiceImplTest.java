package kz.amihady.eccomerce.service;


import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.exception.FileValidationException;
import kz.amihady.eccomerce.image.Status;
import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.repo.ImageRepository;
import kz.amihady.eccomerce.image.service.impl.ImageServiceImpl;
import kz.amihady.eccomerce.image.validation.FileValidator;
import kz.amihady.eccomerce.kafka.image.ImageProducer;
import kz.amihady.eccomerce.kafka.image.event.ImageAddedEvent;
import kz.amihady.eccomerce.kafka.image.event.ImageDeletedEvent;
import kz.amihady.eccomerce.minio.service.MinioImageService;
import kz.amihady.eccomerce.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;


import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MinioImageService minioImageService;

    @Mock
    private ProductService productService;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private ImageProducer imageProducer;

    @InjectMocks
    private ImageServiceImpl imageService;

    private UUID productId;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        file = mock(MultipartFile.class);
    }

    @Test
    void shouldAddImageSuccessfully() {
        // Given
        UUID imageId = UUID.randomUUID();
        String imageUrl = "http://minio.com/" + imageId;

        when(productService.existsById(productId)).thenReturn(true);
        when(minioImageService.uploadImage(any(MultipartFile.class), any(UUID.class))).thenReturn(imageUrl);

        UUID result = imageService.addImage(productId, file);

        assertNotNull(result);
        verify(productService).existsById(productId);
        verify(fileValidator).validate(file);
        verify(imageRepository).save(any(Image.class));
        verify(imageProducer).sendImageAddedEvent(any(ImageAddedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        when(productService.existsById(productId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> imageService.addImage(productId, file));

        verifyNoInteractions(fileValidator, minioImageService, imageRepository, imageProducer);
    }

    @Test
    void addImage_ShouldThrowException_WhenFileValidationFails() {

        when(productService.existsById(productId)).thenReturn(true);
        doThrow(new FileValidationException("Файл не валиден")).when(fileValidator).validate(file);

        assertThrows(FileValidationException.class, () -> imageService.addImage(productId, file));
        verify(imageRepository, never()).save(any());
        verify(imageProducer, never()).sendImageAddedEvent(any());
    }

    @Test
    void deleteImage_Success() {
        // given
        UUID imageId = UUID.randomUUID();
        Image image = new Image();
        image.setId(imageId);
        image.setStatus(Status.ACTIVE);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        // when
        imageService.deleteImage(imageId);

        // then
        assertEquals(Status.DELETED, image.getStatus());
        verify(imageRepository).findById(imageId);
        verify(imageProducer).sendImageDeletedEvent(new ImageDeletedEvent(imageId));
    }

    @Test
    void deleteImagesForProduct_ShouldMarkImagesAsDeleted() {
        Image image1 = new Image();
        image1.setId(UUID.randomUUID());
        image1.setStatus(Status.ACTIVE);
        image1.setProductId(productId);

        Image image2 = new Image();
        image2.setId(UUID.randomUUID());
        image2.setStatus(Status.ACTIVE);
        image2.setProductId(productId);

        when(imageRepository.findByProductId(productId)).thenReturn(List.of(image1, image2));

        imageService.deleteImagesForProduct(productId);

        assertEquals(Status.DELETED, image1.getStatus());
        assertEquals(Status.DELETED, image2.getStatus());
        verify(imageRepository, times(1)).findByProductId(productId);
    }
}
