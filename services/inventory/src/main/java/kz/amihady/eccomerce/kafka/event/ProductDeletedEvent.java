package kz.amihady.eccomerce.kafka.event;

import java.util.UUID;

public record ProductDeletedEvent(
        UUID id
) {
}
