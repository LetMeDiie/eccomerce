package kz.amihady.eccomerce.kafka.consumer.event;

import java.util.UUID;

public record OrderReserveResponseEvent(
        UUID orderId,
        boolean status,
        String message
) {
}
