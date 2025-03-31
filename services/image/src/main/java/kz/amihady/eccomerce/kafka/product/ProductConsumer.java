package kz.amihady.eccomerce.kafka.product;


import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.image.service.ImageService;
import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.product.event.ProductCreatedEvent;
import kz.amihady.eccomerce.kafka.product.event.ProductDeletedEvent;
import kz.amihady.eccomerce.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ProductConsumer {
    KafkaTopicsProperties kafkaTopicsProperties;
    ProductService productService;
    ImageService imageService;


    @KafkaListener(topics = "#{kafkaTopicsProperties.productCreated}")
    public void consumeProductCreatedEvent(Message<ProductCreatedEvent> message) {
        try {
            ProductCreatedEvent event = message.getPayload();
            log.info("Получено событие создания продукта с ID: {}", event.id());
            var product = productService.create(event.id());
            log.info("Событие успешно обработано.");
        } catch (Exception e) {
            log.error("Ошибка обработки события создания продукта", e);
        }
    }


    @KafkaListener(topics = "#{kafkaTopicsProperties.productDeleted}")
    public void consumeProductDeletedEvent(ProductDeletedEvent event) {
        try {
            log.info("Получено событие удаления продукта с ID: {}", event.id());
            productService.delete(event.id());
            imageService.deleteImagesForProduct(event.id());
            log.info("Событие успешно получено и было обработано");
        } catch (MessagingException e) {
            log.error("Ошибка при обработке события удаления продукта с ID: {}", event.id(), e);
        }
        catch (EntityNotFoundException exception) {
            log.error("Ошибка при обработке события удаления продукта, продукт не найден с ID: {}", event.id());
        }
    }
}
