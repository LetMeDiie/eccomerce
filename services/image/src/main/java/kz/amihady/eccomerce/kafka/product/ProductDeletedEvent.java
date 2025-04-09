package kz.amihady.eccomerce.kafka.product;

import java.util.UUID;

public record ProductDeletedEvent(
        UUID id
) {
}
