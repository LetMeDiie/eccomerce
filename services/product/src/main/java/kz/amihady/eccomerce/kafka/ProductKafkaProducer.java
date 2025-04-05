package kz.amihady.eccomerce.kafka;


import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.product.ProductCreatedEvent;
import kz.amihady.eccomerce.kafka.product.ProductDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties kafkaTopicsProperties;

    public void sendProductCreatedEvent(ProductCreatedEvent event) {
        log.info("📤 Отправка события ProductCreatedEvent: {}", event);
        log.info("Топик события: "+kafkaTopicsProperties.getProductCreated());

        Message<ProductCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, kafkaTopicsProperties.getProductCreated())
                .build();

        kafkaTemplate.send(message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Ошибка отправки ProductCreatedEvent", ex);
            } else {
                log.info("✅ Событие успешно отправлен");
            }
        });
    }

    public void sendProductDeletedEvent(ProductDeletedEvent event) {
        log.info("📤 Отправка события ProductDeletedEvent: {}", event);
        log.info("Топик события: "+kafkaTopicsProperties.getProductDeleted());

        Message<ProductDeletedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, kafkaTopicsProperties.getProductDeleted())
                .build();

        kafkaTemplate.send(message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Ошибка отправки ProductDeletedEvent", ex);
            } else {
                log.info("✅ Соыбтие успешно отправлено");
            }
        });
    }
}