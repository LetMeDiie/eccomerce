package kz.amihady.eccomerce.kafka.producer.event;

import java.util.UUID;

public record ImageDeletedEvent(
        UUID id
) {
}
