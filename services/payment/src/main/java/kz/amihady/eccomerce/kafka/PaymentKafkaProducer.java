package kz.amihady.eccomerce.kafka;

import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.event.PaymentInventoryEvent;
import kz.amihady.eccomerce.kafka.event.PaymentOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private final KafkaTopicsProperties kafkaTopicsProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentInventoryEvent(PaymentInventoryEvent event) {
        sendEvent(event,kafkaTopicsProperties.getPaymentInventory());
    }

    public void sendPaymentOrderEvent(PaymentOrderEvent event){
        sendEvent(event,kafkaTopicsProperties.getPaymentOrder());
    }

    public void sendEvent(Object event, String topic) {
        log.info("📤 Отправка события в Kafka. Топик: {}", topic);
        log.debug("📄 Детали события: {}", event);

        Message<Object> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, topic)
                .build();

        try{
            kafkaTemplate.send(message);
            log.info("✅ Событие успешно отправлено в топик {}", topic);
            }
        catch (Exception exception) {
            log.error("Ошибка при отправке события: "+exception.getMessage());
            throw exception;
        }
    }
}
