package kz.amihady.eccomerce.image.service;

import kz.amihady.eccomerce.exception.MinioException;
import kz.amihady.eccomerce.image.Status;
import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.repo.ImageRepository;
import kz.amihady.eccomerce.minio.service.MinioImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class ImageCleanupService {
    ImageRepository imageRepository;
    MinioImageService minioImageService;

    @Scheduled(fixedRate = 120000)
    public void cleanupDeletedImages() {
        log.info("Запускаю фоновую задачу для удаление изображении помеченных deleted");
        var deletedImages = imageRepository.findByStatus(Status.DELETED);

        for (Image image : deletedImages) {
            try {
                minioImageService.deleteImage(image.getId());

                log.debug("Попытка удалить изображение из базы данных ID:" + image.getId());
                imageRepository.deleteById(image.getId());
                log.info("Изображение полностью было удалено из системы с Id: "+image.getId());

            } catch (MinioException exception) {
                log.info("Не удалось удалить изображение , потом попытка повторится. с ID: " + image.getId());
            }
        }
    }
}
