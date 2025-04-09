package kz.amihady.eccomerce.customer.mapper;


import kz.amihady.eccomerce.customer.entity.Address;
import kz.amihady.eccomerce.customer.request.AddressRequest;
import kz.amihady.eccomerce.customer.response.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address fromRequest(AddressRequest request){
        return Address.builder()
                .street(request.street())
                .houseNumber(request.houseNumber())
                .zipCode(request.zipcode())
                .build();
    }

    public AddressResponse fromAddress(Address address){
        return new AddressResponse(
                address.getStreet(),
                address.getHouseNumber(),
                address.getZipCode());
    }
}
