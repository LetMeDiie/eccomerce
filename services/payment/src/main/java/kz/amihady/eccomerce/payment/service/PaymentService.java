package kz.amihady.eccomerce.payment.service;

import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.kafka.PaymentKafkaProducer;
import kz.amihady.eccomerce.kafka.event.PaymentInventoryEvent;
import kz.amihady.eccomerce.kafka.event.PaymentOrderEvent;
import kz.amihady.eccomerce.order.OrderServiceClient;
import kz.amihady.eccomerce.order.OrderStatus;
import kz.amihady.eccomerce.payment.request.PaymentRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PaymentService {

    PaymentKafkaProducer kafkaProducer;
    OrderServiceClient orderService;

    public String createPayment(PaymentRequest request){
        log.info("Обработка платежа для заказа: {} клиент: {}", request.orderId(), request.customerId());

        var order =
                orderService.findById(request.orderId(),request.customerId());

        log.info("Получен заказ: {} со статусом: {}", order.orderId(), order.orderStatus());

        if(order.orderStatus() != OrderStatus.PLACED) {
            throw  new BusinessException("Нельзя совершить покупку для данного заказа");
        }

        //предположим что здесь реальная логика покупки продукта
        log.info("Выполняем логику покупки продукта: {} в количестве: {}", order.productId(), order.quantity());

        var paymentInventoryEvent =
                new PaymentInventoryEvent(order.productId(),order.quantity());
        var paymentOrderEvent =
                new PaymentOrderEvent(request.orderId());

        kafkaProducer.sendPaymentOrderEvent(paymentOrderEvent);
        kafkaProducer.sendPaymentInventoryEvent(paymentInventoryEvent);

        return "Оплата прошла успешна.";
    }
}
