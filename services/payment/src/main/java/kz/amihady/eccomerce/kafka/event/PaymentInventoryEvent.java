package kz.amihady.eccomerce.kafka.event;

import java.util.UUID;

public record PaymentInventoryEvent(
        UUID productId,
        Long quantity
) {
}
