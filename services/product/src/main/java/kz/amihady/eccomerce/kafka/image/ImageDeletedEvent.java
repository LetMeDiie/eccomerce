package kz.amihady.eccomerce.kafka.image;

import java.util.UUID;

public record ImageDeletedEvent(
        UUID id
) {
}