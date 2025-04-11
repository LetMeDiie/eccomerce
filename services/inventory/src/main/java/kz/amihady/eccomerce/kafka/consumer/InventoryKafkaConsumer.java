package kz.amihady.eccomerce.kafka.consumer;


import kz.amihady.eccomerce.inventory.service.InventoryService;
import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.consumer.event.*;
import kz.amihady.eccomerce.kafka.producer.InventoryKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.OrderReserveResponseEvent;
import kz.amihady.eccomerce.inventory.service.PurchaseInventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class InventoryKafkaConsumer {
    InventoryService inventoryService;
    PurchaseInventoryService purchaseInventoryService;
    KafkaTopicsProperties kafkaTopicsProperties;
    InventoryKafkaProducer inventoryKafkaProducer;

    @KafkaListener(topics = "#{kafkaTopicsProperties.productCreated}")
    public void consumeProductCreatedEvent(ProductCreatedEvent event) {
        log.info("Получено событие создания продукта: {}", event);
        try {
            inventoryService.createProductInventory(event.id(), event.quantity());
            log.info("Успешно обработано событие создания продукта с ID: {}", event.id());
        } catch (Exception e) {
            log.error("Ошибка при обработке события создания продукта с ID: {}", event.id(), e);
        }
    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.productDeleted}")
    public void consumeProductDeletedEvent(ProductDeletedEvent event) {
        log.info("Получено событие удаления продукта: {}", event);
        try {
            inventoryService.deleteProductInventory(event.id());
            log.info("Успешно обработано событие удаления продукта с ID: {}", event.id());
        } catch (Exception e) {
            log.error("Ошибка при обработке события удаления продукта с ID: {}", event.id(), e);
        }
    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.orderCanceled}")
    public void consumeOrderCanceled(OrderCanceledEvent event) {
        log.info("Получено событие OrderCanceled. Продукт UUID: {}, Количество: {}", event.productId(), event.quantity());

        try {
            var inventory = inventoryService.findByProductId(event.productId());
            purchaseInventoryService.cancelPurchase(inventory, event.quantity());
            log.info("Событие OrderCanceled успешно обработано и завершено.");
        } catch (Exception exception) {
            log.error("Ошибка при отмене заказа. Продукт UUID: {}, Количество: {}. Ошибка: {}",
                    event.productId(), event.quantity(), exception.getMessage(), exception);
        }
    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.orderReserveRequest}")
    public void consumeOrderReserveRequestEvent(OrderReserveRequestEvent event) {
        log.info("📥 Получено событие OrderReserveRequestEvent: {}", event);

        boolean status;
        String message;

        try {
            var inventory = inventoryService.findByProductId(event.productId());
            purchaseInventoryService.purchase(inventory, event.quantity());
            status = true;
            message = "✅ Продукт успешно был списан из системы.";
        } catch (Exception ex) {
            log.error("❌ Ошибка при списании товара: {}", ex.getMessage(), ex);
            status = false;
            message = "Ошибка при списании товара: " + ex.getMessage();
        }

        var orderReserveResponseEvent = new OrderReserveResponseEvent(event.orderId(), status, message);
        inventoryKafkaProducer.sendOrderReserveResponseEvent(orderReserveResponseEvent);
    }

    @KafkaListener(topics = "#{kafkaTopicsProperties.paymentInventory}")
    public void consumePaymentInventoryEvent (PaymentInventoryEvent event) {
        log.info("Получено событие PaymentInventoryEvent. Продукт UUID: {}, Количество: {}", event.productId(), event.quantity());
        try {
            var inventory = inventoryService.findByProductId(event.productId());
            purchaseInventoryService.confirmPurchase(inventory,event.quantity());
            log.info("Событие PaymentInventoryeEvent успешно обработано и завершено.");
        } catch (Exception exception) {
            log.error("Ошибка. Продукт UUID: {}, Количество: {}. Ошибка: {}",
                    event.productId(), event.quantity(), exception.getMessage(), exception);
        }
    }
}
