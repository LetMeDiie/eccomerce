package kz.amihady.eccomerce.kafka.product.event;

import java.util.UUID;

public record ProductCreatedEvent(
        UUID id
) {
}
