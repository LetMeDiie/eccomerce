package kz.amihady.eccomerce.customer.response;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstname,
        String lastname,
        String email
) {
}
