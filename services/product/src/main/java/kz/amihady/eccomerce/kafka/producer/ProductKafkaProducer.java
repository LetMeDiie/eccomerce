package kz.amihady.eccomerce.kafka.producer;


import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.producer.event.ProductCreatedEvent;
import kz.amihady.eccomerce.kafka.producer.event.ProductDeletedEvent;
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
        sendEvent(event,kafkaTopicsProperties.getProductCreated());
    }

    public void sendProductDeletedEvent(ProductDeletedEvent event) {
        sendEvent(event, kafkaTopicsProperties.getProductDeleted());
    }

    public void sendEvent(Object event, String topic) {
        log.info("📤 Отправка события в Kafka. Топик: {}", topic);
        log.debug("📄 Детали события: {}", event);

        Message<Object> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, topic)
                .build();

        kafkaTemplate.send(message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Ошибка отправки события в Kafka", ex);
            } else {
                log.info("✅ Событие успешно отправлено в топик {}", topic);
            }
        });
    }
}