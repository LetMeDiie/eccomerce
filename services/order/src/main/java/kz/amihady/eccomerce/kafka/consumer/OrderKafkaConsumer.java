package kz.amihady.eccomerce.kafka.consumer;

import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.consumer.event.OrderReserveResponseEvent;
import kz.amihady.eccomerce.kafka.consumer.event.PaymentOrderEvent;
import kz.amihady.eccomerce.order.OrderStatus;
import kz.amihady.eccomerce.order.service.OrderCommandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class OrderKafkaConsumer {

    KafkaTopicsProperties kafkaTopicsProperties;
    OrderCommandService orderCommandService;

    @KafkaListener(topics = "#{kafkaTopicsProperties.orderReserveResponse}")
    public void consumeOrderReserveResponseEvent(OrderReserveResponseEvent event){
        log.info("Получено событие о резерве заказа: {}", event);
        log.info("Сообщение: {}", event.message());

        try {
            var order = orderCommandService.findById(event.orderId());
            var status = event.status()?OrderStatus.PLACED:OrderStatus.FAILED;
            orderCommandService.changeOrderStatus(order,status);
        }
        catch (Exception e){
            log.error("Ошибка при обработке события резерв с ID {}: {}", event.orderId(), e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.paymentOrder}")
    public void consumePaymentOrderEvent(PaymentOrderEvent event){
        log.info("Получено событие оплаты заказа. ID заказа: {}", event.orderId());

        try {
            var order = orderCommandService.findById(event.orderId());
            orderCommandService.changeOrderStatus(order, OrderStatus.PAID);
            log.info("Заказ с ID {} успешно обработан как оплаченный", event.orderId());
        } catch (Exception e) {
            log.error("Ошибка при обработке события оплаты заказа с ID {}: {}", event.orderId(), e.getMessage(), e);
        }
    }

}
