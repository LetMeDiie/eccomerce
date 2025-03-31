package kz.amihady.eccomerce.service;
import static org.mockito.Mockito.*;

import kz.amihady.eccomerce.exception.MinioException;
import kz.amihady.eccomerce.image.Status;
import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.repo.ImageRepository;
import kz.amihady.eccomerce.image.service.ImageCleanupService;
import kz.amihady.eccomerce.minio.service.MinioImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ImageCleanupServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MinioImageService minioImageService;

    @Mock
    private Logger log;

    @InjectMocks
    private ImageCleanupService imageCleanupService;

    @Test
    void cleanupDeletedImages_ShouldDeleteImages_WhenImagesAreMarkedAsDeleted() {
        UUID imageId1 = UUID.randomUUID();
        UUID imageId2 = UUID.randomUUID();
        Image image1 = new Image(imageId1, null,Status.DELETED);
        Image image2 = new Image(imageId2, null,Status.DELETED);

        when(imageRepository.findByStatus(Status.DELETED)).thenReturn(List.of(image1, image2));
        doNothing().when(minioImageService).deleteImage(any(UUID.class));
        doNothing().when(imageRepository).deleteById(any(UUID.class));

        imageCleanupService.cleanupDeletedImages();

        verify(minioImageService, times(1)).deleteImage(imageId1);
        verify(minioImageService, times(1)).deleteImage(imageId2);
        verify(imageRepository, times(1)).deleteById(imageId1);
        verify(imageRepository, times(1)).deleteById(imageId2);
    }

    @Test
    void cleanupDeletedImages_ShouldNotDeleteImage_WhenMinioDeletionFails() {
        UUID imageId = UUID.randomUUID();
        Image image = new Image(imageId,null, Status.DELETED);

        when(imageRepository.findByStatus(Status.DELETED)).thenReturn(List.of(image));
        doThrow(new MinioException("Failed to delete"))
                .when(minioImageService).deleteImage(imageId);

        imageCleanupService.cleanupDeletedImages();

        verify(imageRepository, never()).deleteById(imageId);
    }
}