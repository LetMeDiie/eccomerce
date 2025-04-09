package kz.amihady.eccomerce.order.response;

import kz.amihady.eccomerce.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID productId,
        Long quantity,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        LocalDateTime createdAt
) {
}
