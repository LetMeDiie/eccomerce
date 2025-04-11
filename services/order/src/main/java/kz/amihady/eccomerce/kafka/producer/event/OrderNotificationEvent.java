package kz.amihady.eccomerce.kafka.producer.event;


import java.util.UUID;

public record OrderNotificationEvent(
        String name,
        String message,
        String email,
        UUID orderId,
        UUID productId,
        UUID customerId
) {
}