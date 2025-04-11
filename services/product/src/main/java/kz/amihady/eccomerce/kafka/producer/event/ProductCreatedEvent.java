package kz.amihady.eccomerce.kafka.producer.event;

import java.util.UUID;

public record ProductCreatedEvent(
        UUID id,
        Long quantity
) {
}
