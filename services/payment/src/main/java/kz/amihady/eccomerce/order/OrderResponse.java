package kz.amihady.eccomerce.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID productId,
        Long quantity,
        BigDecimal totalPrice,
        OrderStatus orderStatus
) {
}
