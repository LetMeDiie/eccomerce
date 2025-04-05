package kz.amihady.eccomerce.kafka;

import kz.amihady.eccomerce.image.request.ImageRequest;
import kz.amihady.eccomerce.image.service.ImageService;
import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.image.ImageAddedEvent;
import kz.amihady.eccomerce.kafka.image.ImageDeletedEvent;
import kz.amihady.eccomerce.kafka.inventory.InventoryUpdatedEvent;
import kz.amihady.eccomerce.product.service.ProductCommandService;
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
public class ProductKafkaConsumer {
    KafkaTopicsProperties kafkaTopicsProperties;
    ProductCommandService productCommandService;
    ImageService imageService;


    @KafkaListener(topics = "#{kafkaTopicsProperties.imageAdded}")
    public void consumeImageAddedEvent(ImageAddedEvent event){
        log.info("Получено событие добавления изображения: id={}, productId={}, imageUrl={}",
                event.id(), event.productId(), event.imageUrl());

        ImageRequest request = new ImageRequest(event.id(),event.productId(), event.imageUrl());
        imageService.addImage(request);

        log.info("Событие успешно обработано.");


    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.imageDeleted}")
    public void consumeImageAddedEvent(ImageDeletedEvent event){
        log.info("Получено событие удаления изображения: id={}", event.id());
        imageService.deleteImage(event.id());
        log.info("Событие успешно обработано.");

    }


    @KafkaListener(topics = "#{kafkaTopicsProperties.inventoryUpdated}")
    public void consumeInventoryUpdatedEvent(InventoryUpdatedEvent event){
        log.info("Получено событие на обновление количество продукта: id={} на количество={}", event.productId(),event.quantity());
        productCommandService.updateProductStock(event.productId(), event.quantity());
        log.info("Событие успешно обработано.");
    }
}
