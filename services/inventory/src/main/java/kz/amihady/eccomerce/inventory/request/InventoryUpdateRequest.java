package kz.amihady.eccomerce.inventory.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryUpdateRequest(
        @NotNull(message = "Количество не может быть отсутствовать")
        @Min(value = 1, message = "Количество должно быть больше нуля")
        Long quantity
) {
}
