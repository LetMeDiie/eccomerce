package kz.amihady.eccomerce.product.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdateResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price
) {
}
