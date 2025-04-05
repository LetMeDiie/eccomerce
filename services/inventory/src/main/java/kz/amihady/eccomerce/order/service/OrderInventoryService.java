package kz.amihady.eccomerce.order.service;

import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.inventory.entity.Inventory;
import kz.amihady.eccomerce.inventory.repo.InventoryRepository;
import kz.amihady.eccomerce.kafka.InventoryKafkaProducer;
import kz.amihady.eccomerce.kafka.event.InventoryUpdatedEvent;
import kz.amihady.eccomerce.order.request.OrderInventoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryKafkaProducer inventoryKafkaProducer;

    @Transactional
    public void purchase(OrderInventoryRequest request) {
        log.info("Поступил запрос на покупку продукта: {} в количестве: {}", request.productId(), request.quantity());

        var inventory = findByProductId(request.productId());

        if (inventory.getInStock() < request.quantity()) {
            String message = String.format("Недостаточно товара. Доступно: %d, Запрашиваемое количество: %d", inventory.getInStock(), request.quantity());
            log.warn(message);
            throw new BusinessException(message);
        }


        inventory.setReserved(inventory.getReserved() + request.quantity());
        inventory.setInStock(inventory.getInStock() - request.quantity());

        log.info("Обновление запасов: запас уменьшен на {}, резерв увеличен на {}",
                request.quantity(), request.quantity());

        inventoryRepository.save(inventory);
        log.info("Покупка завершена успешно для продукта: {}", request.productId());

        log.info("Создание и отправка событие об обновлении количество продукта.");
        InventoryUpdatedEvent event = new InventoryUpdatedEvent(inventory.getProductId(),inventory.getInStock());
        inventoryKafkaProducer.sendInventoryUpdatedEvent(event);
    }

    @Transactional
    public void cancelPurchase(OrderInventoryRequest request) {
        log.info("Поступил запрос на отмену покупки продукта: {} в количестве: {}",
                request.productId(), request.quantity());

        var inventory = findByProductId(request.productId());

        if (inventory.getReserved() < request.quantity()) {
            String message = String.format(
                    "Невозможно вернуть товар обратно. Доступно в резерве: %d, Возвращаемое количество: %d",
                    inventory.getReserved(), request.quantity()
            );
            log.warn(message);
            throw new BusinessException(message);
        }

        inventory.setInStock(inventory.getInStock() + request.quantity());
        inventory.setReserved(inventory.getReserved() - request.quantity());

        log.info("Обновление запасов: запас увеличен на {}, резерв уменьшен на {}",
                request.quantity(), request.quantity());

        inventoryRepository.save(inventory);
        log.info("Отмена покупки завершена успешно для продукта: {}", request.productId());

        log.info("Создание и отправка событие об обновлении количество продукта.");
        InventoryUpdatedEvent event = new InventoryUpdatedEvent(inventory.getProductId(),inventory.getInStock());
        inventoryKafkaProducer.sendInventoryUpdatedEvent(event);
    }

    @Transactional
    public void confirmPurchase(UUID productId, Long quantity) {
        log.info("Подтверждение покупки: productId={}, quantity={}", productId, quantity);

        var inventory = findByProductId(productId);

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

        log.info("Покупка подтверждена: productId={}, полностью списано {} единиц", productId, quantity);
    }

    private Inventory findByProductId(UUID productId){
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    String message = String.format("Инвентарь для продукта с ID %s не найден.", productId);
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
    }
}
