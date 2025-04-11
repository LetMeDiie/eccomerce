package kz.amihady.eccomerce.kafka.producer;

import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.producer.event.OrderCanceledEvent;
import kz.amihady.eccomerce.kafka.producer.event.OrderNotificationEvent;
import kz.amihady.eccomerce.kafka.producer.event.OrderReserveRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaProducer {
    private final KafkaTopicsProperties kafkaTopicsProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendOrderCanceledEvent(OrderCanceledEvent event) {
        sendEvent(event, kafkaTopicsProperties.getOrderCanceled());
    }

    public void sendOrderReserveRequestEvent(OrderReserveRequestEvent event) {
        sendEvent(event, kafkaTopicsProperties.getOrderReserveRequest());
    }

    public void sendOrderNotificationEvent(OrderNotificationEvent event){
        sendEvent(event,kafkaTopicsProperties.getOrderNotification());
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
