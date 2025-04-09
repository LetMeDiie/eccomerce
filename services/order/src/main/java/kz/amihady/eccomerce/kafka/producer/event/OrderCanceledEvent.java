package kz.amihady.eccomerce.kafka.producer.event;

import java.util.UUID;

public record OrderCanceledEvent(
        UUID productId,
        Long quantity
) {
}
