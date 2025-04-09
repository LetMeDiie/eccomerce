package kz.amihady.eccomerce.kafka.consumer.event;

import java.util.UUID;

public record OrderReserveRequestEvent(
        UUID orderId,
        UUID productId,
        Long quantity
) {
}
