package kz.amihady.eccomerce.kafka.event;

import java.util.UUID;

public record ProductCreatedEvent(
        UUID id,
        Long quantity
) {
}
