package kz.amihady.eccomerce.service;


import jakarta.annotation.PostConstruct;
import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.inventory.entity.Inventory;
import kz.amihady.eccomerce.inventory.repo.InventoryRepository;
import kz.amihady.eccomerce.inventory.request.InventoryUpdateRequest;
import kz.amihady.eccomerce.inventory.service.InventoryService;
import kz.amihady.eccomerce.kafka.producer.InventoryKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.InventoryUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private InventoryRepository repository;

    @Mock
    private InventoryKafkaProducer kafkaProducer;

    @InjectMocks
    private InventoryService inventoryService;

    private UUID productId;

    @PostConstruct
    public void setUp() {
        productId = UUID.randomUUID();
    }

    @Test
    void updateProductQuantity_Success() {
        var inventory =
                new Inventory(1L, productId, 10L, 3L);
        var inventoryUpdatedRequest
                = new InventoryUpdateRequest(8L);

        when(repository.findByProductId(inventory.getProductId()))
                .thenReturn(Optional.of(inventory));
        when(repository.save(any())).thenReturn(inventory);

        Long updatedStock = inventoryService.updateProductQuantity(productId, inventoryUpdatedRequest);

        assertEquals(5L, updatedStock);
        verify(repository).save(inventory);
        verify(kafkaProducer).sendInventoryUpdatedEvent(any(InventoryUpdatedEvent.class));

    }

    @Test
    void updateProductQuantity_ShouldThrowEntityNotFoundException_WhenInventoryNotFound() {
        var request = new InventoryUpdateRequest(10L);

        when(repository.findByProductId(productId))
                .thenThrow(new EntityNotFoundException("Не найдено"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                inventoryService.updateProductQuantity(productId, request)
        );

        assertEquals("Не найдено", exception.getMessage());
        verify(repository, never()).save(any());
        verify(kafkaProducer, never()).sendInventoryUpdatedEvent(any());
    }

    @Test
    void updateProductQuantity_ShouldThrowBusinessException_WhenReservedMoreThanNewQuantity() {
        var inventory =
                new Inventory(1L, productId, 5L, 5L);
        var request =
                new InventoryUpdateRequest(4L);

        String errorMessage = String.format(
                "Невозможно установить количество товара на складе меньше, чем количество зарезервированных товаров. " +
                        "Запрашиваемое количество: %d, зарезервировано: %d",
                request.quantity(), inventory.getReserved());

        when(repository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                inventoryService.updateProductQuantity(productId, request)
        );

        assertEquals(errorMessage, exception.getMessage());
        verify(repository, never()).save(any());
        verify(kafkaProducer, never()).sendInventoryUpdatedEvent(any());

    }

    @Test
    public void getProductQuantity_Success() {
        var inventory =
                new Inventory(1L, productId, 10L, 5L);

        when(repository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        Long result = inventoryService.getProductQuantity(productId);

        assertEquals(10L, result);
    }

    @Test
    public void getProductQuantity_ShouldThrowEntityNotFoundException_WhenInventoryNotFound() {
        when(repository.findByProductId(productId))
                .thenThrow(new EntityNotFoundException("Не найдено"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                inventoryService.getProductQuantity(productId)
        );

        assertEquals("Не найдено", exception.getMessage());
    }

    @Test
    public void deleteProductInventory_Success() {
        var inventory =
                new Inventory(1L, productId, 10L, 5L);

        when(repository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        inventoryService.deleteProductInventory(productId);

        verify(repository).delete(inventory);
    }

    @Test
    public void deleteProductInventory_ShouldThrowEntityNotFoundException_WhenInventoryNotFound() {
        when(repository.findByProductId(productId))
                .thenThrow(new EntityNotFoundException("Не найдено"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                inventoryService.deleteProductInventory(productId)
        );

        assertEquals("Не найдено", exception.getMessage());
    }
}
