package kz.amihady.eccomerce.kafka.producer.event;

import java.util.UUID;

public record InventoryUpdatedEvent(
        UUID productId,
        Long quantity
) {
}
