package kz.amihady.eccomerce.payment.request;

import java.util.UUID;

public record PaymentRequest(
        UUID customerId,
        UUID orderId
) {
}
