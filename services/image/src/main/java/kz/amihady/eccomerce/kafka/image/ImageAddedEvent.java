package kz.amihady.eccomerce.kafka.image;

import java.util.UUID;

public record ImageAddedEvent(
        UUID id,
        UUID productId,
        String imageUrl
) {
}
