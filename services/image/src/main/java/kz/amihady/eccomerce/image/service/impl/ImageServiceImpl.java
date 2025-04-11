package kz.amihady.eccomerce.image.service.impl;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.image.Status;
import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.repo.ImageRepository;
import kz.amihady.eccomerce.image.service.ImageService;
import kz.amihady.eccomerce.image.validation.FileValidator;
import kz.amihady.eccomerce.kafka.producer.ImageKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.ImageAddedEvent;
import kz.amihady.eccomerce.kafka.producer.event.ImageDeletedEvent;
import kz.amihady.eccomerce.minio.service.MinioImageService;
import kz.amihady.eccomerce.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ImageServiceImpl implements ImageService {
    ImageRepository imageRepository;
    MinioImageService minioImageService;
    ProductService productService;
    FileValidator fileValidator;
    ImageKafkaProducer imageKafkaProducer;

    @Override
    public UUID addImage(UUID productId, MultipartFile file){
        log.info("Начало добавления изображения для продукта с ID: {}", productId);

        if (!productService.existsById(productId)) {
            log.warn("Продукт с ID {} не найден", productId);
            throw new EntityNotFoundException("Продукта нет");
        }

        fileValidator.validate(file);

        UUID imageId = UUID.randomUUID();
        var image = Image.builder()
                .id(imageId)
                .productId(productId)
                .status(Status.ACTIVE)
                .build();

        log.info("Фото создано id:"+image.getId().toString());

        String imageUrl = minioImageService.uploadImage(file, imageId);

        log.debug("Сохраняем информацию об изображении в базе данных...");
        imageRepository.save(image);
        log.info("Информация об изображении успешно сохранена в базе, ID: {}", image.getId());

        log.info("Готовим событие ImageAddedEvent...");
        ImageAddedEvent event = new ImageAddedEvent(image.getId(), productId, imageUrl);
        imageKafkaProducer.sendImageAddedEvent(event);

        return image.getId();
    }

    @Override
    @Transactional
    public void deleteImage(UUID id) {
        var image = imageRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Изображение не найдено."));

        image.setStatus(Status.DELETED); // транзакцию сохраняет изменение в бд автоматом
        log.info("Изображение отмечено удаленным.");

        log.info("Готовим событие ImageDeletedEvent...");
        ImageDeletedEvent event = new ImageDeletedEvent(id);
        imageKafkaProducer.sendImageDeletedEvent(event);
    }

    @Override
    @Transactional()
    public void deleteImagesForProduct(UUID productId) {
        log.info("Начинаю удаление изображений для продукта с ID: {}", productId);

        var images = imageRepository.findByProductId(productId);
        log.info("Найдено {} изображений для удаления", images.size());

        for(Image image : images) {
            image.setStatus(Status.DELETED);
            log.info("Изображение отмечено удаленным id:"+image.getId());
        }
    }
}
