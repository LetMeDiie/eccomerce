package kz.amihady.eccomerce.order.service;


import kz.amihady.eccomerce.customer.CustomerServiceClient;
import kz.amihady.eccomerce.kafka.producer.OrderKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.OrderNotificationEvent;
import kz.amihady.eccomerce.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderNotificationService {
    private final OrderKafkaProducer orderKafkaProducer;
    private final CustomerServiceClient customerServiceClient;


    @Async
    public void sendOrderNotification(Order order) {
        try {
            var customer = customerServiceClient.findById(order.getCustomerId());

            var orderNotification = new OrderNotificationEvent(
                    customer.firstname(),
                    order.getOrderStatus().getMessage(),
                    customer.email(),
                    order.getId(),
                    order.getProductId(),
                    order.getCustomerId()
            );

            orderKafkaProducer.sendOrderNotificationEvent(orderNotification);
            log.info("Уведомление для заказа ID {} успешно отправлено", order.getId());
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления для заказа ID {}: {}", order.getId(), e.getMessage(), e);
        }
    }
}
