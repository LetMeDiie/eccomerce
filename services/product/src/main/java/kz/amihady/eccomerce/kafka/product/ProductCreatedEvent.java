package kz.amihady.eccomerce.kafka.product;

import java.util.UUID;

public record ProductCreatedEvent(
        UUID id,
        Long quantity
) {
}
