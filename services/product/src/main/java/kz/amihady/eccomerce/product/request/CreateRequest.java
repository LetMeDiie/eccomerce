package kz.amihady.eccomerce.product.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateRequest(
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов")
        String name,

        @Size(max = 500, message = "Описание не может превышать 500 символов")
        String description,

        @NotNull(message = "Цена не может быть пустой")
        @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
        @DecimalMax(value = "1000000.0", message = "Цена не может превышать 1 000 000")
        BigDecimal price,

        @NotNull(message = "Количество не может быть пустым")
        @Min(value = 1, message = "Количество должно быть хотя бы 1")
        Long quantity
) {
}
