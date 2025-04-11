package kz.amihady.eccomerce.kafka.consumer.event;


import java.util.UUID;

public record InventoryUpdatedEvent(
        UUID productId,
        Long quantity
) {
}