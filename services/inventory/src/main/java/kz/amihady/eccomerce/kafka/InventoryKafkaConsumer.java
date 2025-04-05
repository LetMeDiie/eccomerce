package kz.amihady.eccomerce.kafka;


import kz.amihady.eccomerce.inventory.service.InventoryService;
import kz.amihady.eccomerce.kafka.config.KafkaTopicsProperties;
import kz.amihady.eccomerce.kafka.event.OrderPaidEvent;
import kz.amihady.eccomerce.kafka.event.ProductCreatedEvent;
import kz.amihady.eccomerce.kafka.event.ProductDeletedEvent;
import kz.amihady.eccomerce.order.service.OrderInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryKafkaConsumer {
    private final InventoryService inventoryService;
    private final OrderInventoryService orderInventoryService;
    private final KafkaTopicsProperties kafkaTopicsProperties;

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

    @KafkaListener(topics = "#{kafkaTopicsProperties.orderPaid}")
    public void consumeOrderPaidEvent(OrderPaidEvent event) {
        log.info("Получено событие OrderPaid. Продукт UUID: {}, Количество: {}", event.productId(), event.quantity());

        try {
            orderInventoryService.confirmPurchase(event.productId(), event.quantity());
            log.info("Успешно подтверждена покупка. Продукт UUID: {}, Количество: {}", event.productId(), event.quantity());
        } catch (Exception exception) {
            log.error("Ошибка при подтверждении покупки. Продукт UUID: {}, Количество: {}. Ошибка: {}",
                    event.productId(), event.quantity(), exception.getMessage(), exception);
        }
    }
}



