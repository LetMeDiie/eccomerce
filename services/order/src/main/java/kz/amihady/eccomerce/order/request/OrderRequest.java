package kz.amihady.eccomerce.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderRequest(
        @NotNull(message = "Идентификатор клиента не может быть null")
        UUID customerId,

        @NotNull(message = "Идентификатор продукта не может быть null")
        UUID productId,

        @NotNull(message = "Количество не может быть null")
        @Min(value = 1, message = "Количество должно быть больше нуля")
        Long quantity
) {
}
