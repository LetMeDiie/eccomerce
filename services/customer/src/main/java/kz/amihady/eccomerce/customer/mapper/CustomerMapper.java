package kz.amihady.eccomerce.customer.mapper;

import kz.amihady.eccomerce.customer.entity.Customer;
import kz.amihady.eccomerce.customer.request.CustomerRequest;
import kz.amihady.eccomerce.customer.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    public Customer fromRequest(CustomerRequest request){
        return Customer.builder()
                .email(request.email())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail()
        );
    }
}
