package kz.amihady.eccomerce.kafka.image.event;

import java.util.UUID;

public record ImageAddedEvent(
        UUID id,
        UUID productId,
        String imageUrl
) {
}
