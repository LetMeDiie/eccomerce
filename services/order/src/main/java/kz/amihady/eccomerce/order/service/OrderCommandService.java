package kz.amihady.eccomerce.order.service;

import kz.amihady.eccomerce.customer.CustomerServiceClient;
import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.kafka.producer.OrderKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.OrderCanceledEvent;
import kz.amihady.eccomerce.kafka.producer.event.OrderReserveRequestEvent;
import kz.amihady.eccomerce.order.OrderStatus;
import kz.amihady.eccomerce.order.entity.Order;
import kz.amihady.eccomerce.order.mapper.OrderMapper;
import kz.amihady.eccomerce.order.repo.OrderRepository;
import kz.amihady.eccomerce.order.request.OrderRequest;
import kz.amihady.eccomerce.product.ProductServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class OrderCommandService {
    CustomerServiceClient customerServiceClient;
    ProductServiceClient productServiceClient;
    OrderMapper orderMapper;
    OrderRepository orderRepository;
    OrderKafkaProducer orderKafkaProducer;
    OrderNotificationService orderNotificationService;


    public UUID createOrder(OrderRequest request) {
        BigDecimal productPrice =
                productServiceClient.getProductPrice(request.productId());
        log.info("Получена цена продукта: "+ productPrice);

        var customer =
                customerServiceClient.findById(request.customerId());
        log.info("Имя клиента: "+customer.firstname());

        BigDecimal totalPrice = productPrice.multiply(
                new BigDecimal(request.quantity()));

        var order = orderMapper.toOrder(request, OrderStatus.PENDING, totalPrice);

        order = orderRepository.save(order);
        log.info("Заказ успешно быз создан и сохранен ID: " + order.getId());

        log.info("Отправка событие что бы зарезервировать продукт");
        var orderReserveRequestEvent =
                new OrderReserveRequestEvent(
                        order.getId(),
                        order.getProductId(),
                        order.getQuantity()
                );
        orderKafkaProducer.sendOrderReserveRequestEvent(orderReserveRequestEvent);

        return order.getId();

    }

    public void cancelOrder(UUID id ) {
        log.info("Получен запрос на отмену заказа с ID: {}", id);

        var order = findById(id);

        if (order.getOrderStatus() != OrderStatus.PLACED) {
            String errorMessage = String.format("Невозможно отменить заказ с ID %s. Статус заказа: %s", id, order.getOrderStatus());
            log.warn(errorMessage);
            throw new BusinessException(errorMessage);
        }

        changeOrderStatus(order,OrderStatus.CANCELED);

        log.info("Заказ с ID {} успешно отменен", id);

        log.info("Отправка событие для отмены товара,заказа с ID: {} (Продукт ID: {}, Количество: {})",
                order.getId(), order.getProductId(), order.getQuantity());

        var orderCanceledEvent = new OrderCanceledEvent(order.getProductId(), order.getQuantity());
        orderKafkaProducer.sendOrderCanceledEvent(orderCanceledEvent);
    }


    @Transactional
    public void changeOrderStatus(Order order , OrderStatus orderStatus){
        log.info("Заказ статуса в данный момент {}",order.getOrderStatus());
        log.info("Меняем статус заказа с ID {} на {}", order.getId(),orderStatus);

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Заказ статуса успешно изменен");

        log.info("Отправка уведомлении об заказе");
        orderNotificationService.sendOrderNotification(order);
    }

    public Order findById(UUID id){
        var order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Заказ с ID {} не найден в базе данных.", id);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });

        log.debug("Найден заказ с ID: {}. Статус заказа: {}", id, order.getOrderStatus());
        return order;
    }
}
