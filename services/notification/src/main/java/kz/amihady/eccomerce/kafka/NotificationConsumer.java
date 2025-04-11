package kz.amihady.eccomerce.kafka;

import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.event.OrderNotificationEvent;
import kz.amihady.eccomerce.notification.entity.Notification;
import kz.amihady.eccomerce.notification.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final KafkaTopicsProperties kafkaTopicsProperties;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "#{kafkaTopicsProperties.orderNotification}")
    public void consumeOrderReserveResponseEvent(OrderNotificationEvent event){
        log.info("Получено событие OrderNotificationEvent: {}", event);
        log.info("Сообщение: {}", event.message());

        try {
           var notification =
                   Notification.builder()
                           .orderId(event.orderId())
                           .email(event.email())
                           .name(event.name())
                           .productId(event.productId())
                           .message(event.message())
                           .customerId(event.customerId())
                           .build();
           notification = notificationRepository.save(notification);
           log.info("Уведомление получено и успешно сохранено в БД id:" + notification.getId());
        }
        catch (Exception e){
            log.error("Ошибка при обработке события "+event, e);
        }
    }
}
