package kz.amihady.eccomerce.product.response;

import kz.amihady.eccomerce.image.response.ImageResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        String name,
        String description,
        BigDecimal price,
        List<ImageResponse> images
) {
}
