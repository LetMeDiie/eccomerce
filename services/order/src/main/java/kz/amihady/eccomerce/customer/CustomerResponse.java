package kz.amihady.eccomerce.customer;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstname,
        String lastname,
        String email
) {
}
