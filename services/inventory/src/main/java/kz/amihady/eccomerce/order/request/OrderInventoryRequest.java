package kz.amihady.eccomerce.order.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderInventoryRequest(
        @NotNull(message = "Идентификатор продукта не может быть пустым")
        UUID productId,

        @Positive(message = "Количество должно быть больше нуля")
        Long quantity
) {
}