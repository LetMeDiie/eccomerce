package kz.amihady.eccomerce.kafka;

import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.event.InventoryUpdatedEvent;
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
public class InventoryKafkaProducer {
    private final KafkaTopicsProperties kafkaTopicsProperties;
    private final KafkaTemplate<String,InventoryUpdatedEvent> kafkaTemplate;

    public void sendInventoryUpdatedEvent(InventoryUpdatedEvent event) {
        log.info("📤 Отправка события InventoryUpdatedEvent: {}", event);
        log.info("Топик события: "+kafkaTopicsProperties.getProductCreated());

        Message<InventoryUpdatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, kafkaTopicsProperties.getInventoryUpdated())
                .build();

        kafkaTemplate.send(message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Ошибка отправки InventoryUpdatedEvent", ex);
            } else {
                log.info("✅ Событие успешно отправлен");
            }
        });
    }

}
