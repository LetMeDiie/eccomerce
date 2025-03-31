package kz.amihady.eccomerce.kafka.product.event;

import java.util.UUID;

public record ProductDeletedEvent(
        UUID id
) {
}
