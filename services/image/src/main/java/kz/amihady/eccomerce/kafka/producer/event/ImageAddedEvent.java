package kz.amihady.eccomerce.kafka.producer.event;

import java.util.UUID;

public record ImageAddedEvent(
        UUID id,
        UUID productId,
        String imageUrl
) {
}
