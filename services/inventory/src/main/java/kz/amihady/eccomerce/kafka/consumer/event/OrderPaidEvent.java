package kz.amihady.eccomerce.kafka.consumer.event;

import java.util.UUID;

public record OrderPaidEvent(
        UUID productId,
        Long quantity
) {
}
