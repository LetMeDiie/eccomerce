package kz.amihady.eccomerce.kafka.producer.event;

import java.util.UUID;

public record OrderReserveResponseEvent(
        UUID orderId,
        boolean status,
        String message
) {
}
