package kz.amihady.eccomerce.kafka.inventory;


import java.util.UUID;

public record InventoryUpdatedEvent(
        UUID productId,
        Long quantity
) {
}