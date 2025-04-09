package kz.amihady.eccomerce.kafka;


import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.image.ImageAddedEvent;
import kz.amihady.eccomerce.kafka.image.ImageDeletedEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class ImageKafkaProducer {

     KafkaTemplate<String, Object> kafkaTemplate;
     KafkaTopicsProperties kafkaTopicsProperties;


    public void sendImageAddedEvent(ImageAddedEvent event) {
        log.info("📤 Отправка ImageAddedEvent: {}", event);

        Message<ImageAddedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC , kafkaTopicsProperties.getImageAdded())
                .build();

        kafkaTemplate.send(message);
        log.info("Событие ImageAddedEvent отправлено: {}", event);

    }

    public void sendImageDeletedEvent(ImageDeletedEvent event) {
        log.info("📤 Отправка ImageDeletedEvent: {}", event);

        Message<ImageDeletedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC , kafkaTopicsProperties.getImageDeleted())
                .build();

        kafkaTemplate.send(message);
        log.info("Событие ImageDeletedEvent отправлено: {}", event);

    }
}
