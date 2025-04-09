package kz.amihady.eccomerce.inventory.service;

import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.inventory.entity.Inventory;
import kz.amihady.eccomerce.inventory.repo.InventoryRepository;
import kz.amihady.eccomerce.kafka.producer.InventoryKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.InventoryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryKafkaProducer inventoryKafkaProducer;

    @Transactional
    public void purchase(Inventory inventory, Long quantity) {
        log.info("Поступил запрос на покупку продукта: {} в количестве: {}", inventory.getProductId(), quantity);


        if (inventory.getInStock() < quantity) {
            String message = String.format("Недостаточно товара. Доступно: %d, Запрашиваемое количество: %d", inventory.getInStock(), quantity);
            log.warn(message);
            throw new BusinessException(message);
        }


        inventory.setReserved(inventory.getReserved() + quantity);
        inventory.setInStock(inventory.getInStock() - quantity);

        log.info("Обновление запасов: запас уменьшен на {}, резерв увеличен на {}",
                quantity, quantity);

        inventoryRepository.save(inventory);
        log.info("Покупка завершена успешно для продукта: {}", inventory.getProductId());

        log.info("Создание и отправка событие об обновлении количество продукта.");
        InventoryUpdatedEvent event = new InventoryUpdatedEvent(inventory.getProductId(),inventory.getInStock());
        inventoryKafkaProducer.sendInventoryUpdatedEvent(event);
    }

    @Transactional
    public void cancelPurchase(Inventory inventory, Long quantity) {
        log.info("Поступил запрос на отмену покупки продукта: {} в количестве: {}",
                inventory.getProductId(), quantity);

        if (inventory.getReserved() < quantity) {
            String message = String.format(
                    "Невозможно вернуть товар обратно. Доступно в резерве: %d, Возвращаемое количество: %d",
                    inventory.getReserved(), quantity
            );
            log.warn(message);
            throw new BusinessException(message);
        }

        inventory.setInStock(inventory.getInStock() + quantity);
        inventory.setReserved(inventory.getReserved() - quantity);

        log.info("Обновление запасов: запас увеличен на {}, резерв уменьшен на {}",
                quantity, quantity);

        inventoryRepository.save(inventory);
        log.info("Отмена покупки завершена успешно для продукта: {}", inventory.getProductId());

        log.info("Создание и отправка событие об обновлении количество продукта.");
        InventoryUpdatedEvent event = new InventoryUpdatedEvent(inventory.getProductId(),inventory.getInStock());
        inventoryKafkaProducer.sendInventoryUpdatedEvent(event);
    }

    @Transactional
    public void confirmPurchase(Inventory inventory, Long quantity) {
        log.info("Подтверждение покупки: productId={}, quantity={}", inventory.getProductId(), quantity);

        if (inventory.getReserved() < quantity) {
            String message = String.format(
                    "Ошибка: попытка списания %d единиц, но в резерве только %d.",
                    quantity, inventory.getReserved()
            );
            log.warn(message);
            throw new BusinessException(message);
        }

        inventory.setReserved(inventory.getReserved() - quantity);
        inventoryRepository.save(inventory);

        log.info("Покупка подтверждена: productId={}, полностью списано {} единиц", inventory.getProductId(), quantity);
    }
}
