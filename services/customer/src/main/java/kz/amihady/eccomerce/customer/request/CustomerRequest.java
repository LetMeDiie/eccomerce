package kz.amihady.eccomerce.customer.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CustomerRequest(
        @NotBlank(message = "Имя не может быть пустым")
        String firstname,

        @NotBlank(message = "Фамилия не может быть пустой")
        String lastname,

        @Email(message = "Email должен быть корректным")
        @NotBlank(message = "Email не может быть пустым")
        String email,

        @NotNull(message = "Адрес не может быть пустым")
        AddressRequest address
) {
}
