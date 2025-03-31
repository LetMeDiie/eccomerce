package kz.amihady.eccomerce.image.request;

import java.util.UUID;

public record ImageRequest(
        UUID imageId,
        UUID productId,
        String imageUrl
) {
}
