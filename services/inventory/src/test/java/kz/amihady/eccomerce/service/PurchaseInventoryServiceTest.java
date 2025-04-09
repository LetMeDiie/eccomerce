package kz.amihady.eccomerce.service;

import jakarta.annotation.PostConstruct;
import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.inventory.entity.Inventory;
import kz.amihady.eccomerce.inventory.repo.InventoryRepository;
import kz.amihady.eccomerce.kafka.producer.InventoryKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.InventoryUpdatedEvent;
import kz.amihady.eccomerce.order.request.OrderInventoryRequest;
import kz.amihady.eccomerce.inventory.service.PurchaseInventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseInventoryServiceTest {

    @Mock
    private  InventoryRepository inventoryRepository;

    @Mock
    private  InventoryKafkaProducer inventoryKafkaProducer;

    @InjectMocks
    private PurchaseInventoryService purchaseInventoryService;

    private UUID productId;

    @PostConstruct
    public void setUp(){
        productId=UUID.randomUUID();
    }


    @Test
    public void purchase_Success(){
        var inventory =
                new Inventory(1L,productId,10L,3L);
        var request =
                new OrderInventoryRequest(productId,4L);

        when(inventoryRepository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        purchaseInventoryService.purchase(request);

        assertEquals(6L , inventory.getInStock());
        assertEquals(7L, inventory.getReserved());
        verify(inventoryRepository).save(inventory);
        verify(inventoryKafkaProducer).sendInventoryUpdatedEvent(any(InventoryUpdatedEvent.class));
    }


    @Test
    public void purchase_NotEnoughStock_ThrowBusinessException(){
        var inventory =
                new Inventory(1L,productId,5L,5L);
        var request =
                new OrderInventoryRequest(productId,6L);
        String message = String.format("Недостаточно товара. Доступно: %d, Запрашиваемое количество: %d", inventory.getInStock(), request.quantity());

        when(inventoryRepository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        BusinessException exception = assertThrows(BusinessException.class, () -> purchaseInventoryService.purchase(request));

        assertEquals(message,exception.getMessage());
        verify(inventoryRepository,never()).save(any());
        verify(inventoryKafkaProducer,never()).sendInventoryUpdatedEvent(any());
    }

    @Test
    public void cancelPurchase_Success(){
        var inventory =
                new Inventory(1L,productId,10L,4L);
        Long quantity =2L;

        when(inventoryRepository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        purchaseInventoryService.cancelPurchase(productId,quantity);

        assertEquals(12L,inventory.getInStock());
        assertEquals(2L,inventory.getReserved());
        verify(inventoryRepository).save(inventory);
        verify(inventoryKafkaProducer).sendInventoryUpdatedEvent(any());
    }

    @Test
    public void cancelPurchase_WhenReservedLessThanQuantity_ThrowBusinessException(){
        var inventory =
                new Inventory(1L,productId,5L,5L);
        Long quantity = 7L;

        String message = String.format(
                "Невозможно вернуть товар обратно. Доступно в резерве: %d, Возвращаемое количество: %d",
                inventory.getReserved(), quantity
        );

        when(inventoryRepository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        BusinessException exception = assertThrows(BusinessException.class, () -> purchaseInventoryService.cancelPurchase(productId,quantity));

        assertEquals(message,exception.getMessage());
        verify(inventoryRepository,never()).save(any());
        verify(inventoryKafkaProducer,never()).sendInventoryUpdatedEvent(any());
    }

    @Test
    public void confirmPurchase_Success(){
        var inventory =
                new Inventory(1L,productId,5L,5L);

        when(inventoryRepository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        purchaseInventoryService.confirmPurchase(productId,4L);

        assertEquals(5L,inventory.getInStock());
        assertEquals(1L,inventory.getReserved());

        verify(inventoryRepository).save(inventory);
    }

    @Test
    public void confirmPurchase_ThrowBusinessException_WhenReservedLessThanQuantity(){
        var inventory =
                new Inventory(1L,productId,5L,2L);
        Long quantity = 4L;
        String message = String.format(
                "Ошибка: попытка списания %d единиц, но в резерве только %d.",
                quantity, inventory.getReserved()
        );

        when(inventoryRepository.findByProductId(productId))
                .thenReturn(Optional.of(inventory));

        BusinessException exception = assertThrows(BusinessException.class, () -> purchaseInventoryService.confirmPurchase(productId,quantity));

        assertEquals(message,exception.getMessage());
        assertEquals(5L,inventory.getInStock());
        assertEquals(2L,inventory.getReserved());

        verify(inventoryRepository,never()).save(any());
    }


}
