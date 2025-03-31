package kz.amihady.eccomerce.kafka.image.event;

import java.util.UUID;

public record ImageDeletedEvent(
        UUID id
) {
}
