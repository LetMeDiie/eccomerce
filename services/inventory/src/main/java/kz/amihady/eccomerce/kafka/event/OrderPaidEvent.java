package kz.amihady.eccomerce.kafka.event;

import java.util.UUID;

public record OrderPaidEvent(
        UUID productId,
        Long quantity
) {
}
