package kz.amihady.eccomerce.kafka.image;

import kz.amihady.eccomerce.image.request.ImageRequest;
import kz.amihady.eccomerce.image.service.ImageService;
import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.image.event.ImageAddedEvent;
import kz.amihady.eccomerce.kafka.image.event.ImageDeletedEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class ImageKafkaConsumer {
    KafkaTopicsProperties kafkaTopicsProperties;
    ImageService imageService;


    @KafkaListener(topics = "#{kafkaTopicsProperties.imageAdded}")
    public void consumeImageAddedEvent(ImageAddedEvent event){
        log.info("Получено событие добавления изображения: id={}, productId={}, imageUrl={}",
                event.id(), event.productId(), event.imageUrl());

        ImageRequest request = new ImageRequest(event.id(),event.productId(), event.imageUrl());
        imageService.addImage(request);


    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.imageDeleted}")
    public void consumeImageAddedEvent(ImageDeletedEvent event){
        log.info("Получено событие удаления изображения: id={}", event.id());
        imageService.deleteImage(event.id());
    }

}
