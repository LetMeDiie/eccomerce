package kz.amihady.eccomerce.kafka.consumer.event;

import java.util.UUID;

public record OrderCanceledEvent(
        UUID productId,
        Long quantity
) {
}
