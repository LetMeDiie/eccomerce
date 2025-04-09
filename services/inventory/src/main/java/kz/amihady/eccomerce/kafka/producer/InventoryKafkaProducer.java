package kz.amihady.eccomerce.kafka.producer;

import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.producer.event.InventoryUpdatedEvent;
import kz.amihady.eccomerce.kafka.producer.event.OrderReserveResponseEvent;
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
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendInventoryUpdatedEvent(InventoryUpdatedEvent event) {
        sendEvent(kafkaTopicsProperties.getInventoryUpdated(),event);
    }

    public void sendOrderReserveResponseEvent (OrderReserveResponseEvent event){
        sendEvent(kafkaTopicsProperties.getOrderReserveResponse(),event);
    }


    public void sendEvent(String topic, Object event) {
        log.info("📤 Отправка события: {}", event);
        log.info("Топик события: {}", topic);

        Message<Object> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, topic)
                .build();

        kafkaTemplate.send(message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Ошибка отправки события", ex);
            } else {
                log.info("✅ Событие успешно отправлено");
            }
        });
    }

}
