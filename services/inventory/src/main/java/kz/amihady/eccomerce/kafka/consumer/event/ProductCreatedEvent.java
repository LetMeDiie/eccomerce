package kz.amihady.eccomerce.kafka.consumer.event;

import java.util.UUID;

public record ProductCreatedEvent(
        UUID id,
        Long quantity
) {
}
