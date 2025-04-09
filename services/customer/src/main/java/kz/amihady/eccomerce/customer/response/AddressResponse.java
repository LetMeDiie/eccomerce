package kz.amihady.eccomerce.customer.response;

public record AddressResponse(
        String street,
        String houseNumber,
        String zipCode
) {
}
