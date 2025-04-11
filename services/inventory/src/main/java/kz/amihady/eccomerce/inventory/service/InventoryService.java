package kz.amihady.eccomerce.inventory.service;

import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.inventory.entity.Inventory;
import kz.amihady.eccomerce.inventory.repo.InventoryRepository;
import kz.amihady.eccomerce.inventory.request.InventoryUpdateRequest;
import kz.amihady.eccomerce.kafka.producer.InventoryKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.InventoryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository repository;
    private final InventoryKafkaProducer kafkaProducer;

    @Transactional
    public Long updateProductQuantity(UUID productId, InventoryUpdateRequest request) {
        log.info("Попытка обновить количество товара для продукта с ID: {}", productId);

        var inventory = findByProductId(productId);

        long newInStock = request.quantity() - inventory.getReserved();

        if (newInStock < 0) {
            String errorMessage = String.format(
                    "Невозможно установить количество товара на складе меньше, чем количество зарезервированных товаров. " +
                            "Запрашиваемое количество: %d, зарезервировано: %d",
                    request.quantity(), inventory.getReserved()
            );
            log.error(errorMessage);
            throw new BusinessException(errorMessage);
        }

        log.info("Обновляем количество товара на складе с {} на {}", inventory.getInStock(), newInStock);

        inventory.setInStock(newInStock);

        repository.save(inventory);
        log.info("Количество товара на складе для продукта с ID {} обновлено успешно. Новое количество: {}",
                productId, newInStock);

        log.info("Создание и отправка событие об обновлении количество продукта.");
        InventoryUpdatedEvent event = new InventoryUpdatedEvent(inventory.getProductId(),inventory.getInStock());
        kafkaProducer.sendInventoryUpdatedEvent(event);

        return inventory.getInStock();
    }


    public Long createProductInventory(UUID productId, Long quantity)  {
        log.info("Создание инвентаря для продукта с ID: {}", productId);

        Inventory inventory = Inventory.builder()
                .productId(productId)
                .inStock(quantity)
                .reserved(0L)
                .build();

        repository.save(inventory);

        log.info("Инвентарь для продукта с ID: {} успешно создан и сохранен в бд.", productId);
        return inventory.getId();
    }

    public void deleteProductInventory(UUID productId) throws EntityNotFoundException{
        log.info("Попытка удалить инвентарь для продукта с ID: {}", productId);
        repository.delete(findByProductId(productId));
        log.info("Инвентарь для продукта с ID: {} успешно удален.", productId);
    }

    public Inventory findByProductId(UUID productId){
        return repository.findByProductId(productId)
                .orElseThrow(() -> {
                    String message = String.format("Инвентарь для продукта с ID %s не найден.", productId);
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
    }
}
