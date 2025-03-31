package kz.amihady.eccomerce.product.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateRequest(
        @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
        String name,

        @Size(max = 500, message = "Описание не должно превышать 500 символов")
        String description,

        @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
        @DecimalMax(value = "1000000.0", message = "Цена не может превышать 1 000 000")
        BigDecimal price
) {
}