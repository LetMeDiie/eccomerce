package kz.amihady.eccomerce.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Улица не может быть пустой")
        String street,

        @NotBlank(message = "Номер дома не может быть пустым")
        String houseNumber,

        @NotBlank(message = "Почтовый индекс не может быть пустым")
        @Size(min = 5, max = 10, message = "Почтовый индекс должен быть от 5 до 10 символов")
        String zipcode
) {
}
